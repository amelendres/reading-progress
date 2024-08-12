package org.appto.readingprogress.domain;

import org.appto.common.domain.DomainException;

class AlreadyReadDevotionalException extends RuntimeException implements DomainException {
    public AlreadyReadDevotionalException(DevotionalId devotionalId) {
        super(String.format("Devotional <%s> is already read", devotionalId));
    }
}
