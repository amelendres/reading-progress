package org.appto.readingprogress.domain;

import org.appto.common.domain.DomainException;

class CannotFinishPlanWithUnreadDevotionalException extends RuntimeException implements DomainException {

    public CannotFinishPlanWithUnreadDevotionalException(PlanId planId) {
        super(String.format("Cannot finish plan <%s> with not read devotionals", planId));
    }
}
