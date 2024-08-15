package org.appto.readingprogress.domain;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ReadingProgressRepository extends CrudRepository<ReadingProgress, ReadingProgressId> {
    default ReadingProgress findByIdOrThrow(String readingProgressId){
        return findById(new ReadingProgressId(readingProgressId)).orElseThrow();
    }

    Optional<ReadingProgress> findByPlanIdAndReaderId(PlanId planId, ReaderId readerId);

}
