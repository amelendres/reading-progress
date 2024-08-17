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
            assertTrue(readingProgress.contentReadings().isEmpty());
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
            assertTrue(readingProgress.contentReadings().isEmpty());
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
            assertTrue(readingProgress.contentReadings().isEmpty());
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
    class OpenContent {

        @Test
        void testSuccessOpenContent() {
            var readingProgress = ReadingProgressFactory.startPlan();
            var devotionalId = new ContentId("de54378b-d101-4864-bc4d-471a0d03f300");
            var openDateContent = Instant.now().atOffset(ZoneOffset.UTC);

            readingProgress.openContent(devotionalId, openDateContent);

            assertEquals(1, readingProgress.contentReadings().size());
            assertTrue(readingProgress.contentReading(devotionalId).isOpen());
            assertFalse(readingProgress.contentReading(devotionalId).isRead());
            assertSame(openDateContent, readingProgress.contentReading(devotionalId).lastOpenedDate());
        }

        @Test
        void testFailsOpenContentUnStartedPlan() {
            var readingProgress = ReadingProgressFactory.openPlan();
            var devotionalId = new ContentId("de54378b-d101-4864-bc4d-471a0d03f300");
            var openDateContent = Instant.now().atOffset(ZoneOffset.UTC);

            assertThrows(CannotOpenContentOnNotStartedPlanException.class, () -> {
                readingProgress.openContent(devotionalId, openDateContent);
            });
        }

        @Test
        void testFailsOpenContentBeforePlanStartedDate() {
            var readingProgress = ReadingProgressFactory.startPlan();
            var devotionalId = new ContentId("de54378b-d101-4864-bc4d-471a0d03f300");
            var pastDate = readingProgress.startDate().minusDays(1L);

            assertThrows(CannotOpenContentBeforePlanStartDateException.class, () -> {
                readingProgress.openContent(devotionalId, pastDate);
            });
        }

        @Test
        void testSuccessReOpenContent() {
            var readingProgress = ReadingProgressFactory.startPlan(
                    Instant.now().atOffset(ZoneOffset.UTC).minusDays(10L),
                    Instant.now().atOffset(ZoneOffset.UTC).minusDays(5L)
            );
            var devotionalId = new ContentId("de54378b-d101-4864-bc4d-471a0d03f300");
            var openDateContent = Instant.now().atOffset(ZoneOffset.UTC).minusDays(4L);
            readingProgress.openContent(devotionalId, openDateContent);

            var reOpenDateContent = Instant.now().atOffset(ZoneOffset.UTC).minusDays(3L);
            readingProgress.openContent(devotionalId, reOpenDateContent);

            assertSame(reOpenDateContent, readingProgress.contentReading(devotionalId).lastOpenedDate());
            assertTrue(readingProgress.contentReading(devotionalId).isOpen());
            assertFalse(readingProgress.contentReading(devotionalId).isRead());
        }

        @Test
        void testFailReOpenContentOnThePast() {
            var readingProgress = ReadingProgressFactory.startPlan(
                    Instant.now().atOffset(ZoneOffset.UTC).minusDays(10L),
                    Instant.now().atOffset(ZoneOffset.UTC).minusDays(5L)
            );
            var devotionalId = new ContentId("de54378b-d101-4864-bc4d-471a0d03f300");
            var openDateContent = Instant.now().atOffset(ZoneOffset.UTC);
            readingProgress.openContent(devotionalId, openDateContent);
            var reOpenDateContent = Instant.now().atOffset(ZoneOffset.UTC).minusDays(4L);

            assertThrows(CannotOpenContentOnThePastException.class, () -> {
                readingProgress.openContent(devotionalId, reOpenDateContent);
            });
        }
    }

    @Nested
    class ReadContent {
        @Test
        void testSuccessReadContent() {
            //given
            var readingProgress = ReadingProgressFactory.startPlan(
                    Instant.now().atOffset(ZoneOffset.UTC).minusDays(10L),
                    Instant.now().atOffset(ZoneOffset.UTC).minusDays(5L)
            );
            var devotionalId = new ContentId("de50378b-d101-4864-bc4d-471a0d03f300");
            var openDateContent = Instant.now().atOffset(ZoneOffset.UTC).minusDays(4L);
            var otherContentId = new ContentId("de60378b-d101-4864-bc4d-471a0d03f300");
            var openDateOtherContent = Instant.now().atOffset(ZoneOffset.UTC).minusDays(3L);

            readingProgress.openContent(devotionalId, openDateContent);
            readingProgress.openContent(otherContentId, openDateOtherContent);

            var readDateContent = Instant.now().atOffset(ZoneOffset.UTC).minusDays(2L);

            //when
            readingProgress.readContent(devotionalId, readDateContent);

            //then
            assertEquals(2, readingProgress.contentReadings().size());
            assertTrue(readingProgress.contentReading(devotionalId).isOpen());
            assertTrue(readingProgress.contentReading(devotionalId).isRead());
            assertFalse(readingProgress.contentReading(otherContentId).isRead());
            assertSame(readDateContent, readingProgress.contentReading(devotionalId).readDate());
        }

        @Test
        void testFailsReadContentWhenIsRead() {
            //given
            var readingProgress = ReadingProgressFactory.startPlan(
                    Instant.now().atOffset(ZoneOffset.UTC).minusDays(10L),
                    Instant.now().atOffset(ZoneOffset.UTC).minusDays(5L)
            );
            var devotionalId = new ContentId("de50378b-d101-4864-bc4d-471a0d03f300");
            var openDateContent = Instant.now().atOffset(ZoneOffset.UTC).minusDays(4L);

            readingProgress.openContent(devotionalId, openDateContent);

            var readDateContent = Instant.now().atOffset(ZoneOffset.UTC).minusDays(2L);
            readingProgress.readContent(devotionalId, readDateContent);

            //when/then
            assertThrows(AlreadyReadContentException.class, () -> {
                readingProgress.readContent(devotionalId, readDateContent);
            });
        }

        @Test
        void testFailsReadContentBeforeOpenContent() {
            //given
            var readingProgress = ReadingProgressFactory.startPlan(
                    Instant.now().atOffset(ZoneOffset.UTC).minusDays(10L),
                    Instant.now().atOffset(ZoneOffset.UTC).minusDays(5L)
            );
            var devotionalId = new ContentId("de50378b-d101-4864-bc4d-471a0d03f300");
            var openDateContent = Instant.now().atOffset(ZoneOffset.UTC).minusDays(2L);

            readingProgress.openContent(devotionalId, openDateContent);

            var readDateContent = Instant.now().atOffset(ZoneOffset.UTC).minusDays(3L);

            //when/then
            assertThrows(CannotReadContentBeforeOpenItException.class, () -> {
                readingProgress.readContent(devotionalId, readDateContent);
            });
        }

        @Test
        void testFailsReadContentWhenContentNotExist() {
            var readingProgress = ReadingProgressFactory.startPlan(
                    Instant.now().atOffset(ZoneOffset.UTC).minusDays(10L),
                    Instant.now().atOffset(ZoneOffset.UTC).minusDays(5L)
            );
            var devotionalId = new ContentId("de50378b-d101-4864-bc4d-471a0d03f300");
            var readDateContent = Instant.now().atOffset(ZoneOffset.UTC).minusDays(2L);

            //when/then
            assertThrows(ContentReadingNotFoundException.class, () -> {
                readingProgress.readContent(devotionalId, readDateContent);
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
            var devotionalId = new ContentId("de50378b-d101-4864-bc4d-471a0d03f300");
            var openDateContent = Instant.now().atOffset(ZoneOffset.UTC).minusDays(4L);

            readingProgress.openContent(devotionalId, openDateContent);

            var readDateContent = Instant.now().atOffset(ZoneOffset.UTC).minusDays(3L);

            readingProgress.readContent(devotionalId, readDateContent);

            var endDate = Instant.now().atOffset(ZoneOffset.UTC).minusDays(2L);

            //when
            readingProgress.finishPlan(endDate);

            //then
            assertEquals(1, readingProgress.contentReadings().size());
            assertNotNull(readingProgress.endDate());
            assertTrue(readingProgress.isFinished());
            assertTrue(readingProgress.contentReading(devotionalId).isRead());
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
        void testFailFinishStartedPlanWithoutContentReadings() {
            var readingProgress = ReadingProgressFactory.startPlan();
            var endDate = Instant.now().atOffset(ZoneOffset.UTC);

            //when/then
            assertThrows(CannotFinishPlanWithEmptyContentReadingsException.class, () -> {
                readingProgress.finishPlan(endDate);
            });
        }

        @Test
        void testFailsFinishPlanWhenAContentIsNotRead() {
            //given
            var readingProgress = ReadingProgressFactory.startPlan(
                    Instant.now().atOffset(ZoneOffset.UTC).minusDays(10L),
                    Instant.now().atOffset(ZoneOffset.UTC).minusDays(5L)
            );
            var devotionalId = new ContentId("de50378b-d101-4864-bc4d-471a0d03f300");
            var openDateContent = Instant.now().atOffset(ZoneOffset.UTC).minusDays(4L);

            readingProgress.openContent(devotionalId, openDateContent);

            var endDate = Instant.now().atOffset(ZoneOffset.UTC).minusDays(2L);

            //when/then
            assertThrows(CannotFinishPlanWithNotReadContentException.class, () -> {
                readingProgress.finishPlan(endDate);
            });
        }

        @Test
        void testFailsFinishPlanBeforeContentRead() {
            //given
            var readingProgress = ReadingProgressFactory.startPlan(
                    Instant.now().atOffset(ZoneOffset.UTC).minusDays(10L),
                    Instant.now().atOffset(ZoneOffset.UTC).minusDays(5L)
            );
            var devotionalId = new ContentId("de50378b-d101-4864-bc4d-471a0d03f300");
            var openDateContent = Instant.now().atOffset(ZoneOffset.UTC).minusDays(4L);

            readingProgress.openContent(devotionalId, openDateContent);

            var readDateContent = Instant.now().atOffset(ZoneOffset.UTC).minusDays(2L);
            readingProgress.readContent(devotionalId, readDateContent);

            var endDate = Instant.now().atOffset(ZoneOffset.UTC).minusDays(3L);

            //when/then
            assertThrows(CannotFinishPlanBeforeReadDateException.class, () -> {
                readingProgress.finishPlan(endDate);
            });
        }
    }
}