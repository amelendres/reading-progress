package org.appto.readingprogress.domain;

import org.appto.common.domain.DomainException;

class CannotStartPlanBeforeOpenedDateException extends RuntimeException implements DomainException {

    public CannotStartPlanBeforeOpenedDateException(ReadingProgressId id) {
        super(String.format("Reading Progress <%s> Cannot start plan before open it.", id));
    }
}
