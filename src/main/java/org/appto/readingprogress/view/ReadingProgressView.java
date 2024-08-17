package org.appto.readingprogress.view;


import java.util.List;

public record ReadingProgressView(
        String id,
        String planId,
        String readerId,
        String lastOpenedDate,
        String startDate,
        String endDate,
        List<ContentReadingView> devotionalReadings
) {
}
