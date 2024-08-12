package org.appto.readingprogress.domain;

import org.appto.common.domain.DomainException;

class CannotOpenDevotionalUnStartedPlanException extends RuntimeException implements DomainException {

    public CannotOpenDevotionalUnStartedPlanException(PlanId planId) {
        super(String.format("Cannot open devotional before plan started  <%s>", planId));
    }
}
