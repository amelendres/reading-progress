package org.appto.readingprogress.domain;


import org.appto.common.domain.DomainException;
import org.appto.common.domain.NotFoundException;

public class ReadingProgressNotFoundException extends RuntimeException implements DomainException, NotFoundException {

    public ReadingProgressNotFoundException(String planId, Throwable cause) {
        super(String.format("Reading Progress Plan <%s> not found", planId), cause);
    }
}
