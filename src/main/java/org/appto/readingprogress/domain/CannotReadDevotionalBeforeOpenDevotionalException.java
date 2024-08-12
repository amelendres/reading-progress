package org.appto.readingprogress.domain;

import org.appto.common.domain.DomainException;

class CannotReadDevotionalBeforeOpenDevotionalException extends RuntimeException implements DomainException {

    public CannotReadDevotionalBeforeOpenDevotionalException(DevotionalId devotionalId) {
        super(String.format("Cannot read devotional before open devotional <%s>", devotionalId));
    }
}
