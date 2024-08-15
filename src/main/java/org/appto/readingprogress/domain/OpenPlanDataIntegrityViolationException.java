package org.appto.readingprogress.domain;

import org.appto.common.domain.DomainException;

public class OpenPlanDataIntegrityViolationException extends RuntimeException implements DomainException {

    public OpenPlanDataIntegrityViolationException(String planId) {
        super(String.format("Open Plan <%s> data integrity violation", planId));
    }
}
