package org.appto.readingprogress.application.query;

import org.appto.readingprogress.domain.*;
import org.appto.readingprogress.view.ReadingProgressView;
import org.appto.readingprogress.view.ReadingProgressRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.EmptyResultDataAccessException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

class GetReadingProgressTest {

    private GetReadingProgress getReadingProgress;
    private ReadingProgressRepository repo;

    @BeforeEach
    public void setUp() throws Exception {
        repo = mock(ReadingProgressRepository.class);
        getReadingProgress = new GetReadingProgress(repo);
    }

    @Test
    void shouldGetReadingProgress() {
        var rp = ReadingProgressFactory.openPlan();
        var expectedReadingProgressView = new ReadingProgressView(
                rp.id().toString(),
                rp.planId().toString(),
                rp.readerId().toString(),
                rp.lastOpenedDate().toString(),
                null,
                null,
                null
        );

        doReturn(expectedReadingProgressView)
                .when(repo)
                .findByIdOrThrow(any(ReadingProgressId.class));

        var view = getReadingProgress.execute(rp.id().toString());

        assertEquals(rp.id().toString(), view.id());
        assertEquals(rp.planId().toString(), view.planId());
        assertEquals(rp.readerId().toString(), view.readerId());

        verify(repo).findByIdOrThrow(any(ReadingProgressId.class));
    }

    @Test
    void shouldThrowNotFoundException() {
        var id = "1e8f503d-dfce-4a4b-ae7c-e51f2d5daa67";

        doThrow(EmptyResultDataAccessException.class)
                .when(repo).findByIdOrThrow(any(ReadingProgressId.class));

        assertThrows(ReadingProgressNotFoundException.class,
                () -> getReadingProgress.execute(id)
        );

        verify(repo).findByIdOrThrow(any(ReadingProgressId.class));
    }
}