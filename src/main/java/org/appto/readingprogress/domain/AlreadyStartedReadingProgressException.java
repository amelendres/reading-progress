package org.appto.readingprogress.domain;

import org.appto.common.domain.DomainException;

class AlreadyStartedReadingProgressException extends RuntimeException implements DomainException {
    public AlreadyStartedReadingProgressException(ReadingProgressId id) {
         super(String.format("Reading Progress <%s> is already started", id));
    }
}
