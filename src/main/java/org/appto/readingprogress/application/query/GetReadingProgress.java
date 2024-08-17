package org.appto.readingprogress.application.query;

import org.appto.readingprogress.domain.ReadingProgressId;
import org.appto.readingprogress.domain.ReadingProgressNotFoundException;
import org.appto.readingprogress.view.ReadingProgressView;
import org.appto.readingprogress.view.ReadingProgressRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@Service
public class GetReadingProgress {
    private final ReadingProgressRepository repository;

    public GetReadingProgress(ReadingProgressRepository repository) {
        this.repository = repository;
    }

    public ReadingProgressView execute(String id) {
        try {
            return this.repository.findByIdOrThrow(new ReadingProgressId(id));
        }catch (DataAccessException e){
            throw new ReadingProgressNotFoundException(id, e);
        }
    }
}
