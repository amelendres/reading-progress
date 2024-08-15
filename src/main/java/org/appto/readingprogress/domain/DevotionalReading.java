package org.appto.readingprogress.domain;

import jakarta.persistence.*;

import java.time.OffsetDateTime;


@Entity
@Table(name="T_CONTENT_READING")
public class DevotionalReading {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID")
    private Long entityId;
    @Embedded
    @AttributeOverride(name="value",column=@Column(name="DEVOTIONAL_ID"))
    private DevotionalId devotionalId;
    private OffsetDateTime lastOpenedDate;
    private OffsetDateTime readDate;

    public DevotionalReading() {
    }

    public DevotionalReading(
            DevotionalId devotionalId,
            OffsetDateTime lastOpenedDate
    ) {
        this.devotionalId = devotionalId;
        this.lastOpenedDate = lastOpenedDate;
    }

    public void open(OffsetDateTime openDate) {
        if (openDate.isBefore(lastOpenedDate)) {
            throw new CannotOpenDevotionalOnThePastException(devotionalId, openDate);
        }

        lastOpenedDate = openDate;
    }

    public void read(OffsetDateTime readDate) {
        if (isRead()) {
            throw new AlreadyReadDevotionalException(devotionalId());
        }

        this.readDate = readDate;
    }

    public boolean isOpen() {
        return null != lastOpenedDate;
    }

    public boolean isRead() {
        return null != readDate;
    }

    public DevotionalId devotionalId() {
        return devotionalId;
    }

    public OffsetDateTime lastOpenedDate() {
        return lastOpenedDate;
    }

    public OffsetDateTime readDate() {
        return readDate;
    }
}
