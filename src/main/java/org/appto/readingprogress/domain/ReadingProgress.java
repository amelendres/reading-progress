package org.appto.readingprogress.domain;

import jakarta.persistence.*;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Entity
@Table(name = "T_READING_PROGRESS")
public class ReadingProgress {
    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "ID"))
    private ReadingProgressId id;
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "PLAN_ID"))
    private PlanId planId;
    private OffsetDateTime lastOpenedDate;
    private OffsetDateTime startDate;
    private OffsetDateTime endDate;
    @OneToMany
    @JoinColumn(name = "READING_PROGRESS_ID")
    private Set<ContentReading> contentReadings = new HashSet<>();
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "READER_ID"))
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

        if (!isOpened()) {
            lastOpenedDate = openedDate;
            return;
        }

        if (openedDate.isBefore(lastOpenedDate)) {
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

    public void openContent(ContentId contentId, OffsetDateTime openDate) {
        if (!isStarted()) {
            throw new CannotOpenContentOnNotStartedPlanException(planId());
        }

        if (openDate.isBefore(startDate)) {
            throw new CannotOpenContentBeforePlanStartDateException(planId());
        }

        var contentReading = contentReadingOptional(contentId).orElse(new ContentReading(contentId, openDate));
        contentReading.open(openDate);
        contentReadings.add(contentReading);
    }

    public void readContent(ContentId contentId, OffsetDateTime readDate) {
        var contentReading = contentReading(contentId);

        if (readDate.isBefore(contentReading.lastOpenedDate())) {
            throw new CannotReadContentBeforeOpenItException(contentId);
        }

        contentReading.read(readDate);
    }

    private Optional<ContentReading> contentReadingOptional(ContentId contentId) {

        return contentReadings.stream()
                .filter(item -> item.contentId().equals(contentId))
                .findFirst();
    }

    public ContentReading contentReading(ContentId contentId) {

        return contentReadings.stream()
                .filter(item -> item.contentId().equals(contentId))
                .findFirst().orElseThrow(() -> new ContentReadingNotFoundException(contentId));
    }

    public void finishPlan(OffsetDateTime endDate) {
        if (!isStarted()) {
            throw new CannotFinishUnstartedPlanException(planId());
        }

        if (endDate.isBefore(startDate)) {
            throw new CannotFinishPlanBeforeStartDateException(planId());
        }

        if (contentReadings.isEmpty()) {
            throw new CannotFinishPlanWithEmptyContentReadingsException(planId());
        }

        for (ContentReading contentReading : contentReadings) {
            if (!contentReading.isRead()) {
                throw new CannotFinishPlanWithNotReadContentException(planId());
            }

            if (endDate.isBefore(contentReading.readDate())) {
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

    public Set<ContentReading> contentReadings() {
        return contentReadings;
    }

    public ReaderId readerId() {
        return readerId;
    }
}
