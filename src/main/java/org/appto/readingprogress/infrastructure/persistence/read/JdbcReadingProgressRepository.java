package org.appto.readingprogress.infrastructure.persistence.read;

import org.appto.readingprogress.domain.ReadingProgressId;
import org.appto.readingprogress.view.ReadingProgressRepository;
import org.appto.readingprogress.view.ReadingProgressView;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class JdbcReadingProgressRepository implements ReadingProgressRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcReadingProgressRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    @Override
    public ReadingProgressView findByIdOrThrow(ReadingProgressId id) throws DataAccessException {
        return jdbcTemplate.queryForObject(
                "select rp.* from T_READING_PROGRESS rp where rp.id=?",
                (rs, rowNum) -> new ReadingProgressView(
                        rs.getString("id"),
                        rs.getString("plan_id"),
                        rs.getString("reader_id"),
                        rs.getString("last_opened_date") != null ? rs.getObject("last_opened_date").toString() : null,
                        rs.getString("start_date") != null ? rs.getObject("start_date").toString() : null,
                        rs.getString("end_date") != null ? rs.getObject("end_date").toString() : null,
                        null
                )
                , id.toString());
    }
}

