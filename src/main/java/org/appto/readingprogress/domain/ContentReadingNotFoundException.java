package org.appto.readingprogress.domain;

import org.appto.common.domain.DomainException;
import org.appto.common.domain.NotFoundException;

class ContentReadingNotFoundException extends RuntimeException implements DomainException, NotFoundException {
    public ContentReadingNotFoundException(ContentId contentId) {
        super(String.format("Content reading <%s> not found", contentId));
    }
}
