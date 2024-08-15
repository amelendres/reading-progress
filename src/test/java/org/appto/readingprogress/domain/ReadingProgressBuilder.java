package org.appto.readingprogress.domain;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public class ReadingProgressBuilder {
    //default constructor values
    private final ReadingProgressId id = new ReadingProgressId("1e8f503d-dfce-4a4b-ae7c-e51f2d5daa67");
    private final PlanId planId = new PlanId("f9cfecb1-a608-429c-bad4-2a976dcce4ce");
    private final ReaderId readerId = new ReaderId("cc9da6cd-80a1-4fb9-9aa4-8cb32b7eef9c");
    private OffsetDateTime lastOpenedDate = Instant.now().atOffset(ZoneOffset.UTC);



    //Builder
    public ReadingProgressBuilder openPlan(OffsetDateTime openDate){
        lastOpenedDate = openDate;
        return this;
    }


    //build
    public ReadingProgress build() {
        var readingProgress =  new ReadingProgress(id, planId, readerId);
        readingProgress.openPlan(lastOpenedDate);
        return readingProgress;
    }

}
