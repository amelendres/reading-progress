package org.appto.readingprogress.domain;

import org.appto.common.domain.DomainException;

class CannotFinishPlanBeforeStartDateException extends RuntimeException implements DomainException {

    public CannotFinishPlanBeforeStartDateException(PlanId planId) {
        super(String.format("Cannot finish plan <%s> before start it", planId));
    }
}
