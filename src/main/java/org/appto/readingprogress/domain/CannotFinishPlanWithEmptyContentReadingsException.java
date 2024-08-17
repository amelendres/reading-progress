package org.appto.readingprogress.domain;

import org.appto.common.domain.DomainException;

class CannotFinishPlanWithEmptyContentReadingsException extends RuntimeException implements DomainException {

    public CannotFinishPlanWithEmptyContentReadingsException(PlanId planId) {
        super(String.format("Cannot finish plan <%s> with empty content readings", planId));
    }
}
