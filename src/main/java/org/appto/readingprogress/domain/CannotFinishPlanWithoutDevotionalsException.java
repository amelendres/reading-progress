package org.appto.readingprogress.domain;

import org.appto.common.domain.DomainException;

class CannotFinishPlanWithoutDevotionalsException extends RuntimeException implements DomainException {

    public CannotFinishPlanWithoutDevotionalsException(PlanId planId) {
        super(String.format("Cannot finish plan <%s> without devotionals", planId));
    }
}
