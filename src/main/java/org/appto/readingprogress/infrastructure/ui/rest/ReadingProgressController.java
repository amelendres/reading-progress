package org.appto.readingprogress.infrastructure.ui.rest;

import org.appto.readingprogress.application.command.OpenPlan;
import org.appto.readingprogress.application.query.GetReadingProgress;
import org.appto.readingprogress.domain.OpenPlanDataIntegrityViolationException;
import org.appto.readingprogress.domain.ReadingProgressNotFoundException;
import org.appto.readingprogress.view.ReadingProgressView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeParseException;

@RestController
@RequestMapping("/reading-progress")
public class ReadingProgressController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final OpenPlan openPlan;
    private final GetReadingProgress getReadingProgress;

    public ReadingProgressController(OpenPlan openPlan, GetReadingProgress getReadingProgress) {
        this.openPlan = openPlan;
        this.getReadingProgress = getReadingProgress;
    }

    public record OpenPlanRequest(String planId, String readerId, String openDate) {
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> openPlan(@RequestBody OpenPlanRequest req, @PathVariable String id) {
        openPlan.execute(new OpenPlan.Command(id, req.planId, req.readerId, req.openDate));

        return ResponseEntity
                .noContent()
                .build();
    }

    @GetMapping("/{id}")
    public ReadingProgressView getReadingProgress(@PathVariable String id) {
        return getReadingProgress.execute(id);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(OpenPlanDataIntegrityViolationException.class)
    public void handleConflictException(Exception ex) {
        logger.error("Exception is: ", ex);
    }
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({ReadingProgressNotFoundException.class})
    public void handleNotFound(Exception ex) {
        logger.error("Exception is: ", ex);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({IllegalArgumentException.class, DateTimeParseException.class})
    public void handleBadRequest(Exception ex) {
        logger.error("Exception is: ", ex);
    }
}
