package org.appto.readingprogress.domain;


import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public class ReadingProgressFactory {

    final private static ReadingProgressBuilder builder = new ReadingProgressBuilder();

    public static ReadingProgress openPlan()
    {
        var openedDate = Instant.now().atOffset(ZoneOffset.UTC);

        return openPlan(openedDate);
    }

    public static ReadingProgress openPlan(OffsetDateTime openDate)
    {
        return builder
                .openPlan(openDate)
                .build();
    }

    public static ReadingProgress startPlan()
    {
        var openedDate = Instant.now().minusSeconds(10L).atOffset(ZoneOffset.UTC);
        var startDate = Instant.now().atOffset(ZoneOffset.UTC);

        return startPlan(openedDate, startDate);
    }
    public static ReadingProgress startPlan(OffsetDateTime openDate, OffsetDateTime startDate)
    {
        var readingProgress = openPlan(openDate);
        readingProgress.startPlan(startDate);
        return readingProgress;
    }
}