package org.appto.readingprogress.domain;


import org.appto.common.domain.DomainException;

import java.time.OffsetDateTime;

class CannotOpenDevotionalOnThePastException extends RuntimeException implements DomainException {

    public CannotOpenDevotionalOnThePastException(DevotionalId devotionalId, OffsetDateTime openedDate) {
        super(String.format(
                        "Cannot open devotional <%s> on the past <%s>",
                        devotionalId,
                        openedDate
                )
        );
    }
}
