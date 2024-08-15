package org.appto.readingprogress.domain;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class ReadingProgressIdTest extends ValueObjectTest<ReadingProgressId>{

    @Override
    ReadingProgressId createValue() {
        return new ReadingProgressId("1e8f503d-dfce-4a4b-ae7c-e51f2d5daa67");
    }

    @Override
    ReadingProgressId createOtherValue() {
        return new ReadingProgressId("2e8f503d-dfce-4a4b-ae7c-e51f2d5daa67");
    }

    @ParameterizedTest
    @ValueSource(strings = { "", "no-valid-uuid"})
    @NullSource
    void shouldFailToCreateWithInvalidValue(String value){
        assertThrows(IllegalArgumentException.class,
                () -> new ReadingProgressId(value));
    }
}