package org.appto.readingprogress.domain;

import org.appto.common.domain.DomainException;

class AlreadyReadContentException extends RuntimeException implements DomainException {
    public AlreadyReadContentException(ContentId contentId) {
        super(String.format("Content <%s> is already read", contentId));
    }
}
