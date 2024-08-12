package org.appto.readingprogress.domain;

import java.time.OffsetDateTime;

public class DevotionalReading {
    private DevotionalId devotionalId;
    private OffsetDateTime lastOpenedDate;
    private OffsetDateTime readDate;

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
