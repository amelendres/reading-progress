package org.appto.readingprogress.domain;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ReadingProgressRepository extends CrudRepository<ReadingProgress, ReadingProgressId> {
    default ReadingProgress findByIdOrThrow(ReadingProgressId id){
        return findById(id).orElseThrow();
    }

    Optional<ReadingProgress> findByPlanIdAndReaderId(PlanId planId, ReaderId readerId);

}
