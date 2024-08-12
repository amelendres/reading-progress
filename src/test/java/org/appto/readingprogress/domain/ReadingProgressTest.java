package org.appto.readingprogress.domain;

import java.time.Instant;
import java.time.ZoneOffset;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReadingProgressTest {

    @Nested
    class OpenPlan {

        @Test
        void testSuccessfullyOpensPlan() {
            var readingProgress = ReadingProgressFactory.openPlan();

            assertNotNull(readingProgress.lastOpenedDate());
            assertNull(readingProgress.startDate());
            assertTrue(readingProgress.devotionalReadings().isEmpty());
            assertNotNull(readingProgress.readerId());
            assertNotNull(readingProgress.planId());
        }

        @Test
        void testSuccessfullyReOpenPlan() {
            var readingProgress = ReadingProgressFactory.openPlan();
            var reOpenedDate = readingProgress.lastOpenedDate().plusDays(1L);
            readingProgress.openPlan(reOpenedDate);

            assertSame(readingProgress.lastOpenedDate(), reOpenedDate);
            assertNull(readingProgress.startDate());
            assertTrue(readingProgress.devotionalReadings().isEmpty());
        }

        @Test
        void testFailsToReopenPastPlan() {
            var readingProgress = ReadingProgressFactory.openPlan();
            var pastDate = readingProgress.lastOpenedDate().minusDays(10L);

            assertThrows(CannotOpenPlanOnThePastException.class, () -> {
                readingProgress.openPlan(pastDate);
            });
        }

    }


    @Nested
    class StartPlan {
        @Test
        void testSuccessStartPlan() {
            var readingProgress = ReadingProgressFactory.startPlan();

            assertNotNull(readingProgress.startDate());
            assertNull(readingProgress.endDate());
            assertTrue(readingProgress.devotionalReadings().isEmpty());
        }

        @Test
        void testFailsToStartPlanBeforeOpenedDate() {

            var readingProgress = ReadingProgressFactory.openPlan();
            var pastDate = readingProgress.lastOpenedDate().minusDays(1L);

            assertThrows(CannotStartPlanBeforeOpenedDateException.class, () -> {
                readingProgress.startPlan(pastDate);
            });
        }

        @Test
        void testFailsToReStartAStartedPlan() {

            var readingProgress = ReadingProgressFactory.startPlan();
            var reStartDate = Instant.now().atOffset(ZoneOffset.UTC);

            assertThrows(AlreadyStartedReadingProgressException.class, () -> {
                readingProgress.startPlan(reStartDate);
            });
        }
    }


    @Nested
    class OpenDevotional {

        @Test
        void testSuccessOpenDevotional() {
            var readingProgress = ReadingProgressFactory.startPlan();
            var devotionalId = new DevotionalId("de54378b-d101-4864-bc4d-471a0d03f300");
            var openDateDevotional = Instant.now().atOffset(ZoneOffset.UTC);

            readingProgress.openDevotional(devotionalId, openDateDevotional);

            assertEquals(1, readingProgress.devotionalReadings().size());
            assertTrue(readingProgress.devotionalReading(devotionalId).isOpen());
            assertFalse(readingProgress.devotionalReading(devotionalId).isRead());
            assertSame(openDateDevotional, readingProgress.devotionalReading(devotionalId).lastOpenedDate());
        }

        @Test
        void testFailsOpenDevotionalUnStartedPlan() {
            var readingProgress = ReadingProgressFactory.openPlan();
            var devotionalId = new DevotionalId("de54378b-d101-4864-bc4d-471a0d03f300");
            var openDateDevotional = Instant.now().atOffset(ZoneOffset.UTC);

            assertThrows(CannotOpenDevotionalUnStartedPlanException.class, () -> {
                readingProgress.openDevotional(devotionalId, openDateDevotional);
            });
        }

        @Test
        void testFailsOpenDevotionalBeforePlanStartedDate() {
            var readingProgress = ReadingProgressFactory.startPlan();
            var devotionalId = new DevotionalId("de54378b-d101-4864-bc4d-471a0d03f300");
            var pastDate = readingProgress.startDate().minusDays(1L);

            assertThrows(CannotOpenDevotionalBeforeStartPlanException.class, () -> {
                readingProgress.openDevotional(devotionalId, pastDate);
            });
        }

        @Test
        void testSuccessReOpenDevotional() {
            var readingProgress = ReadingProgressFactory.startPlan(
                    Instant.now().atOffset(ZoneOffset.UTC).minusDays(10L),
                    Instant.now().atOffset(ZoneOffset.UTC).minusDays(5L)
            );
            var devotionalId = new DevotionalId("de54378b-d101-4864-bc4d-471a0d03f300");
            var openDateDevotional = Instant.now().atOffset(ZoneOffset.UTC).minusDays(4L);
            readingProgress.openDevotional(devotionalId, openDateDevotional);

            var reOpenDateDevotional = Instant.now().atOffset(ZoneOffset.UTC).minusDays(3L);
            readingProgress.openDevotional(devotionalId, reOpenDateDevotional);

            assertSame(reOpenDateDevotional, readingProgress.devotionalReading(devotionalId).lastOpenedDate());
            assertTrue(readingProgress.devotionalReading(devotionalId).isOpen());
            assertFalse(readingProgress.devotionalReading(devotionalId).isRead());
        }

        @Test
        void testFailReOpenDevotionalOnThePast() {
            var readingProgress = ReadingProgressFactory.startPlan(
                    Instant.now().atOffset(ZoneOffset.UTC).minusDays(10L),
                    Instant.now().atOffset(ZoneOffset.UTC).minusDays(5L)
            );
            var devotionalId = new DevotionalId("de54378b-d101-4864-bc4d-471a0d03f300");
            var openDateDevotional = Instant.now().atOffset(ZoneOffset.UTC);
            readingProgress.openDevotional(devotionalId, openDateDevotional);
            var reOpenDateDevotional = Instant.now().atOffset(ZoneOffset.UTC).minusDays(4L);

            assertThrows(CannotOpenDevotionalOnThePastException.class, () -> {
                readingProgress.openDevotional(devotionalId, reOpenDateDevotional);
            });
        }
    }

    @Nested
    class ReadDevotional {
        @Test
        void testSuccessReadDevotional() {
            //given
            var readingProgress = ReadingProgressFactory.startPlan(
                    Instant.now().atOffset(ZoneOffset.UTC).minusDays(10L),
                    Instant.now().atOffset(ZoneOffset.UTC).minusDays(5L)
            );
            var devotionalId = new DevotionalId("de50378b-d101-4864-bc4d-471a0d03f300");
            var openDateDevotional = Instant.now().atOffset(ZoneOffset.UTC).minusDays(4L);
            var otherDevotionalId = new DevotionalId("de60378b-d101-4864-bc4d-471a0d03f300");
            var openDateOtherDevotional = Instant.now().atOffset(ZoneOffset.UTC).minusDays(3L);

            readingProgress.openDevotional(devotionalId, openDateDevotional);
            readingProgress.openDevotional(otherDevotionalId, openDateOtherDevotional);

            var readDateDevotional = Instant.now().atOffset(ZoneOffset.UTC).minusDays(2L);

            //when
            readingProgress.readDevotional(devotionalId, readDateDevotional);

            //then
            assertEquals(2, readingProgress.devotionalReadings().size());
            assertTrue(readingProgress.devotionalReading(devotionalId).isOpen());
            assertTrue(readingProgress.devotionalReading(devotionalId).isRead());
            assertFalse(readingProgress.devotionalReading(otherDevotionalId).isRead());
            assertSame(readDateDevotional, readingProgress.devotionalReading(devotionalId).readDate());
        }

        @Test
        void testFailsReadDevotionalWhenIsRead() {
            //given
            var readingProgress = ReadingProgressFactory.startPlan(
                    Instant.now().atOffset(ZoneOffset.UTC).minusDays(10L),
                    Instant.now().atOffset(ZoneOffset.UTC).minusDays(5L)
            );
            var devotionalId = new DevotionalId("de50378b-d101-4864-bc4d-471a0d03f300");
            var openDateDevotional = Instant.now().atOffset(ZoneOffset.UTC).minusDays(4L);

            readingProgress.openDevotional(devotionalId, openDateDevotional);

            var readDateDevotional = Instant.now().atOffset(ZoneOffset.UTC).minusDays(2L);
            readingProgress.readDevotional(devotionalId, readDateDevotional);

            //when/then
            assertThrows(AlreadyReadDevotionalException.class, () -> {
                readingProgress.readDevotional(devotionalId, readDateDevotional);
            });
        }

        @Test
        void testFailsReadDevotionalBeforeOpenDevotional() {
            //given
            var readingProgress = ReadingProgressFactory.startPlan(
                    Instant.now().atOffset(ZoneOffset.UTC).minusDays(10L),
                    Instant.now().atOffset(ZoneOffset.UTC).minusDays(5L)
            );
            var devotionalId = new DevotionalId("de50378b-d101-4864-bc4d-471a0d03f300");
            var openDateDevotional = Instant.now().atOffset(ZoneOffset.UTC).minusDays(2L);

            readingProgress.openDevotional(devotionalId, openDateDevotional);

            var readDateDevotional = Instant.now().atOffset(ZoneOffset.UTC).minusDays(3L);

            //when/then
            assertThrows(CannotReadDevotionalBeforeOpenDevotionalException.class, () -> {
                readingProgress.readDevotional(devotionalId, readDateDevotional);
            });
        }

        @Test
        void testFailsReadDevotionalWhenDevotionalNotExist() {
            var readingProgress = ReadingProgressFactory.startPlan(
                    Instant.now().atOffset(ZoneOffset.UTC).minusDays(10L),
                    Instant.now().atOffset(ZoneOffset.UTC).minusDays(5L)
            );
            var devotionalId = new DevotionalId("de50378b-d101-4864-bc4d-471a0d03f300");
            var readDateDevotional = Instant.now().atOffset(ZoneOffset.UTC).minusDays(2L);

            //when/then
            assertThrows(DevotionalReadingNotFoundException.class, () -> {
                readingProgress.readDevotional(devotionalId, readDateDevotional);
            });
        }
    }


    @Nested
    class FinishPlan {
        @Test
        void testSuccessFinishPlan() {
            //given
            var readingProgress = ReadingProgressFactory.startPlan(
                    Instant.now().atOffset(ZoneOffset.UTC).minusDays(10L),
                    Instant.now().atOffset(ZoneOffset.UTC).minusDays(5L)
            );
            var devotionalId = new DevotionalId("de50378b-d101-4864-bc4d-471a0d03f300");
            var openDateDevotional = Instant.now().atOffset(ZoneOffset.UTC).minusDays(4L);

            readingProgress.openDevotional(devotionalId, openDateDevotional);

            var readDateDevotional = Instant.now().atOffset(ZoneOffset.UTC).minusDays(3L);

            readingProgress.readDevotional(devotionalId, readDateDevotional);

            var endDate = Instant.now().atOffset(ZoneOffset.UTC).minusDays(2L);

            //when
            readingProgress.finishPlan(endDate);

            //then
            assertEquals(1, readingProgress.devotionalReadings().size());
            assertNotNull(readingProgress.endDate());
            assertTrue(readingProgress.isFinished());
            assertTrue(readingProgress.devotionalReading(devotionalId).isRead());
        }

        @Test
        void testFailsToFinishPlanBeforeStartDate() {

            var readingProgress = ReadingProgressFactory.startPlan();
            var endDate = Instant.now().atOffset(ZoneOffset.UTC).minusDays(10L);

            //when/then
            assertThrows(CannotFinishPlanBeforeStartDateException.class, () -> {
                readingProgress.finishPlan(endDate);
            });
        }

        @Test
        void testFailsToFinishNotStartedPlan() {
            var readingProgress = ReadingProgressFactory.openPlan();
            var endDate = Instant.now().atOffset(ZoneOffset.UTC);

            //when/then
            assertThrows(CannotFinishUnstartedPlanException.class, () -> {
                readingProgress.finishPlan(endDate);
            });
        }

        @Test
        void testFailFinishStartedPlanWithoutDevotionalReadings() {
            var readingProgress = ReadingProgressFactory.startPlan();
            var endDate = Instant.now().atOffset(ZoneOffset.UTC);

            //when/then
            assertThrows(CannotFinishPlanWithoutDevotionalsException.class, () -> {
                readingProgress.finishPlan(endDate);
            });
        }

        @Test
        void testFailsFinishPlanWhenADevotionalIsNotRead() {
            //given
            var readingProgress = ReadingProgressFactory.startPlan(
                    Instant.now().atOffset(ZoneOffset.UTC).minusDays(10L),
                    Instant.now().atOffset(ZoneOffset.UTC).minusDays(5L)
            );
            var devotionalId = new DevotionalId("de50378b-d101-4864-bc4d-471a0d03f300");
            var openDateDevotional = Instant.now().atOffset(ZoneOffset.UTC).minusDays(4L);

            readingProgress.openDevotional(devotionalId, openDateDevotional);

            var endDate = Instant.now().atOffset(ZoneOffset.UTC).minusDays(2L);

            //when/then
            assertThrows(CannotFinishPlanWithUnreadDevotionalException.class, () -> {
                readingProgress.finishPlan(endDate);
            });
        }

        @Test
        void testFailsFinishPlanBeforeDevotionalRead() {
            //given
            var readingProgress = ReadingProgressFactory.startPlan(
                    Instant.now().atOffset(ZoneOffset.UTC).minusDays(10L),
                    Instant.now().atOffset(ZoneOffset.UTC).minusDays(5L)
            );
            var devotionalId = new DevotionalId("de50378b-d101-4864-bc4d-471a0d03f300");
            var openDateDevotional = Instant.now().atOffset(ZoneOffset.UTC).minusDays(4L);

            readingProgress.openDevotional(devotionalId, openDateDevotional);

            var readDateDevotional = Instant.now().atOffset(ZoneOffset.UTC).minusDays(2L);
            readingProgress.readDevotional(devotionalId, readDateDevotional);

            var endDate = Instant.now().atOffset(ZoneOffset.UTC).minusDays(3L);

            //when/then
            assertThrows(CannotFinishPlanBeforeReadDateException.class, () -> {
                readingProgress.finishPlan(endDate);
            });
        }
    }
}