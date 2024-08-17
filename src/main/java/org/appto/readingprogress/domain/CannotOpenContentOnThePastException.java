package org.appto.readingprogress.domain;


import org.appto.common.domain.DomainException;

import java.time.OffsetDateTime;

class CannotOpenContentOnThePastException extends RuntimeException implements DomainException {

    public CannotOpenContentOnThePastException(ContentId contentId, OffsetDateTime openedDate) {
        super(String.format(
                        "Cannot open content <%s> on the past <%s>",
                contentId,
                        openedDate
                )
        );
    }
}
