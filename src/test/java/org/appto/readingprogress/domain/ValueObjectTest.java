package org.appto.readingprogress.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

abstract class ValueObjectTest<T> {

    abstract T createValue();
    abstract T createOtherValue();

    @Test
    void shouldSatisfyValueObjectEquality() {
        var first = createValue();
        var second = createValue();
        assertEquals(first, second);
        assertEquals(first.hashCode(), second.hashCode());        
    }

    @Test
    void shouldSatisfyValueObjectInequality() {
        var first = createValue();
        var second = createOtherValue();
        assertNotEquals(first, second);
        assertNotEquals(first.hashCode(), second.hashCode());
    }
}