package org.appto.readingprogress.domain;

public record ContentId(String value){
    public ContentId {
        if (null == value) {
            throw new IllegalArgumentException("Invalid ContentId <null>");
        }
        java.util.UUID.fromString(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
