package org.appto.readingprogress.view;


import org.appto.readingprogress.domain.ReadingProgressId;
import org.springframework.dao.DataAccessException;

public interface ReadingProgressRepository {
    ReadingProgressView findByIdOrThrow(ReadingProgressId id) throws DataAccessException;

}
