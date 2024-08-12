package org.appto.readingprogress.domain;

import org.appto.common.domain.DomainException;
import org.appto.common.domain.NotFoundException;

class DevotionalReadingNotFoundException extends RuntimeException implements DomainException, NotFoundException {
    public DevotionalReadingNotFoundException(DevotionalId devotionalId) {
        super(String.format("Devotional reading <%s> not found", devotionalId));
    }
}
