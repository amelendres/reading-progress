package org.appto.readingprogress.domain;

import org.appto.common.domain.DomainException;

class CannotReadContentBeforeOpenItException extends RuntimeException implements DomainException {

    public CannotReadContentBeforeOpenItException(ContentId contentId) {
        super(String.format("Cannot read content before open it <%s>", contentId));
    }
}
