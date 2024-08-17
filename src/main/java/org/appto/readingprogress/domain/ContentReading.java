package org.appto.readingprogress.domain;

import jakarta.persistence.*;

import java.time.OffsetDateTime;


@Entity
@Table(name="T_CONTENT_READING")
public class ContentReading {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID")
    private Long entityId;
    @Embedded
    @AttributeOverride(name="value",column=@Column(name="CONTENT_ID"))
    private ContentId contentId;
    private OffsetDateTime lastOpenedDate;
    private OffsetDateTime readDate;

    public ContentReading() {
    }

    public ContentReading(
            ContentId contentId,
            OffsetDateTime lastOpenedDate
    ) {
        this.contentId = contentId;
        this.lastOpenedDate = lastOpenedDate;
    }

    public void open(OffsetDateTime openDate) {
        if (openDate.isBefore(lastOpenedDate)) {
            throw new CannotOpenContentOnThePastException(contentId, openDate);
        }

        lastOpenedDate = openDate;
    }

    public void read(OffsetDateTime readDate) {
        if (isRead()) {
            throw new AlreadyReadContentException(contentId());
        }

        this.readDate = readDate;
    }

    public boolean isOpen() {
        return null != lastOpenedDate;
    }

    public boolean isRead() {
        return null != readDate;
    }

    public ContentId contentId() {
        return contentId;
    }

    public OffsetDateTime lastOpenedDate() {
        return lastOpenedDate;
    }

    public OffsetDateTime readDate() {
        return readDate;
    }
}
