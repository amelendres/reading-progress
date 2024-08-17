package org.appto.readingprogress.application.command;

import org.appto.readingprogress.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class OpenPlanTest {

    private OpenPlan openPlan;
    private ReadingProgressRepository repo;

    @BeforeEach
    public void setUp() throws Exception {
        repo = mock(ReadingProgressRepository.class);
        openPlan = new OpenPlan(repo);
    }

    @Test
    void shouldCreateReadingProgressWhenOpenPlanTheFirstTime() {
        var req = new OpenPlan.Command(
                "1e8f503d-dfce-4a4b-ae7c-e51f2d5daa67",
                "f9cfecb1-a608-429c-bad4-2a976dcce4ce",
                "cc9da6cd-80a1-4fb9-9aa4-8cb32b7eef9c",
                Instant.now().atOffset(ZoneOffset.UTC).toString()
        );

        doReturn(Optional.empty())
                .when(repo)
                .findByPlanIdAndReaderId(any(PlanId.class), any(ReaderId.class));

        openPlan.execute(req);

        verify(repo).findByPlanIdAndReaderId(any(PlanId.class), any(ReaderId.class));
        verify(repo).save(any(ReadingProgress.class));

    }

    @Test
    void shouldCreateReadingProgressWithCurrentDateTimeWhenOpenPlanTheFirstTime() {
        var req = new OpenPlan.Command(
                "1e8f503d-dfce-4a4b-ae7c-e51f2d5daa67",
                "f9cfecb1-a608-429c-bad4-2a976dcce4ce",
                "cc9da6cd-80a1-4fb9-9aa4-8cb32b7eef9c",
                null
        );

        doReturn(Optional.empty())
                .when(repo)
                .findByPlanIdAndReaderId(any(PlanId.class), any(ReaderId.class));

        openPlan.execute(req);

        verify(repo).findByPlanIdAndReaderId(any(PlanId.class), any(ReaderId.class));
        verify(repo).save(any(ReadingProgress.class));

    }

    @Test
    void shouldUpdateLastOpenedDateWhenReOpenPlan() {
        var readingProgress = ReadingProgressFactory.openPlan();
        var req = new OpenPlan.Command(
                readingProgress.id().toString(),
                readingProgress.planId().toString(),
                readingProgress.readerId().toString(),
                Instant.now().atOffset(ZoneOffset.UTC).toString()
        );

        doReturn(Optional.of(readingProgress))
                .when(repo)
                .findByPlanIdAndReaderId(any(PlanId.class), any(ReaderId.class));

        openPlan.execute(req);

        verify(repo).findByPlanIdAndReaderId(any(PlanId.class), any(ReaderId.class));
        verify(repo).save(any(ReadingProgress.class));
    }

    @Test
    void shouldFailToOpenPlanWithInconsistentRequest() {
        var readingProgress = ReadingProgressFactory.openPlan();
        var req = new OpenPlan.Command(
                UUID.randomUUID().toString(),
                readingProgress.planId().toString(),
                readingProgress.readerId().toString(),
                Instant.now().atOffset(ZoneOffset.UTC).toString()
        );

        doReturn(Optional.of(readingProgress))
                .when(repo)
                .findByPlanIdAndReaderId(any(PlanId.class), any(ReaderId.class));


        assertThrows(OpenPlanDataIntegrityViolationException.class,
                () -> openPlan.execute(req)
        );

        verify(repo).findByPlanIdAndReaderId(any(PlanId.class), any(ReaderId.class));
    }
}