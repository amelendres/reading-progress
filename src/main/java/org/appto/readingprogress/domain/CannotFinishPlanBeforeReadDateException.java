package org.appto.readingprogress.domain;

import org.appto.common.domain.DomainException;

class CannotFinishPlanBeforeReadDateException extends RuntimeException implements DomainException {

    public CannotFinishPlanBeforeReadDateException(PlanId planId) {
        super(String.format("Cannot finish plan <%s> before read devotionals", planId));
    }
}
