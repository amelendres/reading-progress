package org.appto.readingprogress.domain;

import java.util.UUID;

public record PlanId(String value){
    public PlanId {
        if (null == value) {
            throw new IllegalArgumentException("Invalid PlanId <null>");
        }

       UUID.fromString(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
