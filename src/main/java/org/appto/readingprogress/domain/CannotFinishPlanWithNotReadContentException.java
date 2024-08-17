package org.appto.readingprogress.domain;

import org.appto.common.domain.DomainException;

class CannotFinishPlanWithNotReadContentException extends RuntimeException implements DomainException {

    public CannotFinishPlanWithNotReadContentException(PlanId planId) {
        super(String.format("Cannot finish plan <%s> with not read content", planId));
    }
}
