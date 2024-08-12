package org.appto.readingprogress.domain;

import org.appto.common.domain.DomainException;

class CannotOpenPlanOnThePastException extends RuntimeException implements DomainException {

    public CannotOpenPlanOnThePastException(PlanId planId) {
        super(String.format("Can not open past plan <%s>", planId));
    }
}
