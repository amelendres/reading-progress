package org.appto.readingprogress.domain;

import org.appto.common.domain.DomainException;

class CannotOpenContentOnNotStartedPlanException extends RuntimeException implements DomainException {

    public CannotOpenContentOnNotStartedPlanException(PlanId planId) {
        super(String.format("Cannot open content on not started plan <%s>", planId));
    }
}
