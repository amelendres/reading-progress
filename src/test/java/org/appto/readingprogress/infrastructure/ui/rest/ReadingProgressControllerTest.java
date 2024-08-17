package org.appto.readingprogress.infrastructure.ui.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.appto.readingprogress.application.command.OpenPlan;
import org.appto.readingprogress.application.query.GetReadingProgress;
import org.appto.readingprogress.domain.OpenPlanDataIntegrityViolationException;
import org.appto.readingprogress.domain.ReadingProgressNotFoundException;
import org.appto.readingprogress.view.ReadingProgressView;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.time.ZoneOffset;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReadingProgressController.class)
class ReadingProgressControllerTest {
    private static final String RP_PATH = "/reading-progress/{id}";

    @Autowired
    private MockMvc mvc;

    @MockBean
    private OpenPlan openPlan;
    @MockBean
    private GetReadingProgress getReadinProgress;

    @Nested
    class OpenPlanTest{
        @Test
        void shouldOpenPlan() throws Exception {
            var req = new ReadingProgressController.OpenPlanRequest(
                    "f9cfecb1-a608-429c-bad4-2a976dcce4ce",
                    "cc9da6cd-80a1-4fb9-9aa4-8cb32b7eef9c",
                    Instant.now().atOffset(ZoneOffset.UTC).toString()
            );

            mvc.perform(put(RP_PATH, "1e8f503d-dfce-4a4b-ae7c-e51f2d5daa67")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(req)))
                    .andExpect(status().isNoContent());
        }

        @Test
        void shouldFailToOpenPlanWithInconsistentRequest() throws Exception {
            var req = new ReadingProgressController.OpenPlanRequest(
                    "f9cfecb1-a608-429c-bad4-2a976dcce4ce",
                    "cc9da6cd-80a1-4fb9-9aa4-8cb32b7eef9c",
                    Instant.now().atOffset(ZoneOffset.UTC).toString()
            );

            doThrow(OpenPlanDataIntegrityViolationException.class)
                    .when(openPlan).execute(any(OpenPlan.Command.class));

            mvc.perform(put(RP_PATH, "1e8f503d-dfce-4a4b-ae7c-e51f2d5daa67")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(req)))
                    .andExpect(status().isConflict());

            verify(openPlan).execute(any(OpenPlan.Command.class));
        }
    }


    @Nested
    class GetReadingProgressTest{
        @Test
        void should_Get_ReadingProgress() throws Exception {
            var expectedReadingProgressView = new ReadingProgressView(
                    "1e8f503d-dfce-4a4b-ae7c-e51f2d5daa67",
                    "f9cfecb1-a608-429c-bad4-2a976dcce4ce",
                    "cc9da6cd-80a1-4fb9-9aa4-8cb32b7eef9c",
                    Instant.now().atOffset(ZoneOffset.UTC).toString(),
                    null,
                    null,
                    null
            );

            doReturn(expectedReadingProgressView)
                    .when(getReadinProgress)
                    .execute(anyString());

            mvc.perform(get(RP_PATH, "1e8f503d-dfce-4a4b-ae7c-e51f2d5daa67"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("id").value("1e8f503d-dfce-4a4b-ae7c-e51f2d5daa67"))
                    .andExpect(jsonPath("planId").value("f9cfecb1-a608-429c-bad4-2a976dcce4ce"))
                    .andExpect(jsonPath("readerId").value("cc9da6cd-80a1-4fb9-9aa4-8cb32b7eef9c"));

            verify(getReadinProgress).execute(anyString());
        }

        @Test
        void should_Throw_NotFoundException() throws Exception {

            doThrow(ReadingProgressNotFoundException.class)
                    .when(getReadinProgress).execute(anyString());

            mvc.perform(get(RP_PATH, "1e8f503d-dfce-4a4b-ae7c-e51f2d5daa67"))
                    .andExpect(status().isNotFound());

            verify(getReadinProgress).execute(anyString());
        }
    }


    protected static String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}