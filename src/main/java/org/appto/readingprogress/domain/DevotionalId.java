package org.appto.readingprogress.domain;

public record DevotionalId(String value){
    public DevotionalId {
        if (null == value) {
            throw new IllegalArgumentException("Invalid DevotionalId <null>");
        }
        java.util.UUID.fromString(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
