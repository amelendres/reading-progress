package org.appto.readingprogress.domain;

import org.appto.common.domain.DomainException;

class CannotOpenDevotionalBeforeStartPlanException extends RuntimeException implements DomainException {

    public CannotOpenDevotionalBeforeStartPlanException(PlanId planId) {
        super(String.format("Cannot open devotional before start plan <%s>", planId));
    }
}
