package org.appto.readingprogress.domain;

import java.util.UUID;

public record ReaderId(String value){
    public ReaderId {
        if (null == value) {
            throw new IllegalArgumentException("Invalid ReaderId <null>");
        }

        UUID.fromString(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
