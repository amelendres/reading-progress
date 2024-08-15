package org.appto.readingprogress.application.command;

import org.appto.readingprogress.domain.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;



@Service
public class OpenPlan {
    private final ReadingProgressRepository readingProgressRepository;

    public record Command(String id, String planId, String readerId, String openDate){ }
    public OpenPlan(ReadingProgressRepository readingProgressRepository) {
        this.readingProgressRepository = readingProgressRepository;
    }

    public void execute(Command cmd) {
        var readingProgress = readingProgressRepository.findByPlanIdAndReaderId(
                new PlanId(cmd.planId),
                new ReaderId(cmd.readerId)
        ).orElseGet(() -> new ReadingProgress(
                new ReadingProgressId(cmd.id),
                new PlanId(cmd.planId),
                new ReaderId(cmd.readerId)
        ));

        if (!readingProgress.id().toString().equals(cmd.id)) {
            throw new OpenPlanDataIntegrityViolationException(cmd.id);
        }

        var openDate = null == cmd.openDate
                ? Instant.now().atOffset(ZoneOffset.UTC)
                : OffsetDateTime.parse(cmd.openDate);

        readingProgress.openPlan(openDate);

        readingProgressRepository.save(readingProgress);
    }
}
