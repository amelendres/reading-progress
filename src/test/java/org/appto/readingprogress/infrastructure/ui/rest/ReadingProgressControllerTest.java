package org.appto.readingprogress.infrastructure.ui.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.appto.readingprogress.application.command.OpenPlan;
import org.appto.readingprogress.domain.OpenPlanDataIntegrityViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.time.ZoneOffset;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReadingProgressController.class)
class ReadingProgressControllerTest {
    private static final String RP_PATH = "/reading-progress/{id}";

    @Autowired
    private MockMvc mvc;

    @MockBean
    private OpenPlan openPlan;

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


    protected static String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            final String jsonContent = mapper.writeValueAsString(obj);
            return jsonContent;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}