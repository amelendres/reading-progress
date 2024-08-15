package org.appto.readingprogress.domain;

import jakarta.persistence.*;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "T_READING_PROGRESS")
public class ReadingProgress {
    @EmbeddedId
    @AttributeOverride(name="value",column=@Column(name="ID"))
    private ReadingProgressId id;
    @Embedded
    @AttributeOverride(name="value",column=@Column(name="PLAN_ID"))
    private PlanId planId;
    private OffsetDateTime lastOpenedDate;
    private OffsetDateTime startDate;
    private OffsetDateTime endDate;
    @OneToMany
    @JoinColumn(name = "READING_PROGRESS_ID")
    private Set<DevotionalReading> devotionalReadings = new HashSet<>();
    @Embedded
    @AttributeOverride(name="value",column=@Column(name="READER_ID"))
    private ReaderId readerId;

    public ReadingProgress() {
    }

    public ReadingProgress(
            ReadingProgressId id,
            PlanId planId,
            ReaderId readerId
    ) {
        this.id = id;
        this.planId = planId;
        this.readerId = readerId;
    }

    public void openPlan(OffsetDateTime openedDate) {

        if (!isOpened()){
            lastOpenedDate = openedDate;
            return;
        }

        if(openedDate.isBefore(lastOpenedDate)) {
            throw new CannotOpenPlanOnThePastException(planId);
        }

        lastOpenedDate = openedDate;
    }

    public void startPlan(OffsetDateTime startDate) {
        if (isStarted()) {
            throw new AlreadyStartedReadingProgressException(id);
        }

        if (startDate.isBefore(lastOpenedDate)) {
            throw new CannotStartPlanBeforeOpenedDateException(id());
        }

        this.startDate = startDate;
    }

    public void openDevotional(DevotionalId devotionalId, OffsetDateTime openDate) {
        if (!isStarted()) {
            throw new CannotOpenDevotionalUnStartedPlanException(planId());
        }

        if (openDate.isBefore(startDate)) {
            throw new CannotOpenDevotionalBeforeStartPlanException(planId());
        }

        var devotionalReading = devotionalReading(devotionalId);

        if (null != devotionalReading) {
            devotionalReading.open(openDate);
        } else {
            devotionalReading = new DevotionalReading(devotionalId, openDate);
            devotionalReadings.add(devotionalReading);
        }
    }

    public void readDevotional(DevotionalId devotionalId, OffsetDateTime readDate) {
        var devotionalReading = devotionalReading(devotionalId);
        if (null == devotionalReading) {
            throw new DevotionalReadingNotFoundException(devotionalId);
        }

        if (readDate.isBefore(devotionalReading.lastOpenedDate())) {
            throw new CannotReadDevotionalBeforeOpenDevotionalException(devotionalId);
        }

        devotionalReading.read(readDate);
    }

    public DevotionalReading devotionalReading(DevotionalId devotionalId) {

        return devotionalReadings.stream()
                .filter(dev -> dev.devotionalId().equals(devotionalId))
                .findFirst().orElse(null)
                ;
    }

    public void finishPlan(OffsetDateTime endDate) {
        if (!isStarted()) {
            throw new CannotFinishUnstartedPlanException(planId());
        }

        if (endDate.isBefore(startDate)) {
            throw new CannotFinishPlanBeforeStartDateException(planId());
        }

        if (devotionalReadings.isEmpty()) {
            throw new CannotFinishPlanWithoutDevotionalsException(planId());
        }

        for (DevotionalReading devotionalReading : devotionalReadings) {
            if (!devotionalReading.isRead()) {
                throw new CannotFinishPlanWithUnreadDevotionalException(planId());
            }

            if (endDate.isBefore(devotionalReading.readDate())) {
                throw new CannotFinishPlanBeforeReadDateException(planId());
            }
        }

        this.endDate = endDate;
    }

    public boolean isOpened() {
        return null != lastOpenedDate();
    }

    public boolean isStarted() {
        return null != startDate();
    }

    public boolean isFinished() {
        return null != endDate();
    }

    public ReadingProgressId id() {
        return id;
    }

    public PlanId planId() {
        return planId;
    }

    public OffsetDateTime lastOpenedDate() {
        return lastOpenedDate;
    }

    public OffsetDateTime startDate() {
        return startDate;
    }

    public OffsetDateTime endDate() {
        return endDate;
    }

    public Set<DevotionalReading> devotionalReadings() {
        return devotionalReadings;
    }

    public ReaderId readerId() {
        return readerId;
    }
}
