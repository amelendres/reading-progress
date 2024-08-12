package org.appto.readingprogress.domain;

import org.appto.common.domain.DomainException;

class CannotFinishUnstartedPlanException extends RuntimeException implements DomainException {

    public CannotFinishUnstartedPlanException(PlanId planId) {
        super(String.format("Cannot finish plan <%s> before start it", planId));
    }
}
