package org.appto.readingprogress.domain;

import org.appto.common.domain.DomainException;

class CannotOpenContentBeforePlanStartDateException extends RuntimeException implements DomainException {

    public CannotOpenContentBeforePlanStartDateException(PlanId planId) {
        super(String.format("Cannot open content before the plan's start date <%s>", planId));
    }
}
