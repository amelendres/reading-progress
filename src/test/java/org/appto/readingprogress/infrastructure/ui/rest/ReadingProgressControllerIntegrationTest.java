package org.appto.readingprogress.infrastructure.ui.rest;

import org.appto.readingprogress.application.command.OpenPlan;
import org.appto.readingprogress.view.ReadingProgressView;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReadingProgressControllerIntegrationTest {
    private static final String RT_PATH = "/reading-progress/{id}";

    @Autowired
    private TestRestTemplate restTemplate;

    @Nested
    class OpenPlanTest {

        @Test
        void should_open_plan_and_return_204() {
            var id = "1e8f503d-dfce-4a4b-ae7c-e51f2d5daa67";
            var openPlanRequest = new ReadingProgressController.OpenPlanRequest(
                    "f9cfecb1-a608-429c-bad4-2a976dcce4ce",
                    "cc9da6cd-80a1-4fb9-9aa4-8cb32b7eef9c",
                    Instant.now().atOffset(ZoneOffset.ofHours(2)).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
            );

            HttpEntity<ReadingProgressController.OpenPlanRequest> request = new HttpEntity<>(openPlanRequest);
            ResponseEntity<Void> response = restTemplate.exchange(RT_PATH, HttpMethod.PUT, request, Void.class, id);

            assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

            var expectedReadingProgressView = restTemplate.getForObject(RT_PATH, ReadingProgressView.class, id);

            assertEquals(id, expectedReadingProgressView.id());
            assertEquals(openPlanRequest.planId(), expectedReadingProgressView.planId());
            assertEquals(openPlanRequest.readerId(), expectedReadingProgressView.readerId());
            assertEquals(openPlanRequest.openDate(), expectedReadingProgressView.lastOpenedDate());
        }

        @Test
        @DirtiesContext
        void should_re_open_plan_and_return_204() {
            var id = "1e8f503d-dfce-4a4b-ae7c-e51f2d5daa67";
            var openPlanRequest = new ReadingProgressController.OpenPlanRequest(
                    "f9cfecb1-a608-429c-bad4-2a976dcce4ce",
                    "cc9da6cd-80a1-4fb9-9aa4-8cb32b7eef9c",
                    Instant.now().atOffset(ZoneOffset.UTC).toString()
            );

            HttpEntity<ReadingProgressController.OpenPlanRequest> request = new HttpEntity<>(openPlanRequest);
            ResponseEntity<Void> response = restTemplate.exchange(RT_PATH, HttpMethod.PUT, request, Void.class, id);

            assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

            var reOpenPlanRequest = new OpenPlan.Command(
                    "1e8f503d-dfce-4a4b-ae7c-e51f2d5daa67",
                    "f9cfecb1-a608-429c-bad4-2a976dcce4ce",
                    "cc9da6cd-80a1-4fb9-9aa4-8cb32b7eef9c",
                    Instant.now().atOffset(ZoneOffset.UTC).toString()
            );

            HttpEntity<OpenPlan.Command> request2 = new HttpEntity<>(reOpenPlanRequest);
            ResponseEntity<Void> response2 = restTemplate.exchange(RT_PATH, HttpMethod.PUT, request2, Void.class, id);

            assertEquals(HttpStatus.NO_CONTENT, response2.getStatusCode());
        }


        @ParameterizedTest
        @CsvSource({
                "invalid-id, f9cfecb1-a608-429c-bad4-2a976dcce4ce, cc9da6cd-80a1-4fb9-9aa4-8cb32b7eef9c, 2024-08-16T10:15:30+01:00",
                "1e8f503d-dfce-4a4b-ae7c-e51f2d5daa67, invalid-planId, cc9da6cd-80a1-4fb9-9aa4-8cb32b7eef9c, 2024-08-16T10:15:30+01:00",
                "1e8f503d-dfce-4a4b-ae7c-e51f2d5daa67, f9cfecb1-a608-429c-bad4-2a976dcce4ce, invalid-readerId, 2024-08-16T10:15:30+01:00",
                "1e8f503d-dfce-4a4b-ae7c-e51f2d5daa67, f9cfecb1-a608-429c-bad4-2a976dcce4ce, cc9da6cd-80a1-4fb9-9aa4-8cb32b7eef9c, 2024-08-16T10:15",
        })
        void should_fail_to_open_plan_for_invalid_request_and_return_400(String id, String planId, String readerId, String date) {
            var openPlanRequest = new ReadingProgressController.OpenPlanRequest(
                    planId,
                    readerId,
                    date
            );

            HttpEntity<ReadingProgressController.OpenPlanRequest> request = new HttpEntity<>(openPlanRequest);

            ResponseEntity<Void> response = restTemplate.exchange(RT_PATH, HttpMethod.PUT, request, Void.class, id);

            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        }
    }

    @Nested
    class GetReadingProgressTest {
        @Test
        void should_fail_get_for_invalid_id_and_return_400() {
            var id = "invalid-id";

            ResponseEntity<Void> response = restTemplate.getForEntity(RT_PATH, Void.class, id);

            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        }
        @Test
        void should_fail_get_non_existing_reading_progress_returns_404() {
            var id = "1e8f503d-dfce-4a4b-ae7c-e51f2d5daa67";

            ResponseEntity<Void> response = restTemplate.getForEntity(RT_PATH, Void.class, id);

            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }

    }

}
