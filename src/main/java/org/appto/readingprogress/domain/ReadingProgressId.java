package org.appto.readingprogress.domain;

import java.util.UUID;

public record ReadingProgressId(String value){
    public ReadingProgressId {
        if (null == value) {
            throw new IllegalArgumentException("Invalid UUID <null>");
        }

        UUID.fromString(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
