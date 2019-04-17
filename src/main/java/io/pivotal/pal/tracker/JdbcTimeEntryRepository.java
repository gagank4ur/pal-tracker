package io.pivotal.pal.tracker;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

public class JdbcTimeEntryRepository implements TimeEntryRepository {

    private JdbcTemplate jdbcTemplate;

    public JdbcTimeEntryRepository(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public TimeEntry create(TimeEntry timeEntry) {
        String query = "INSERT INTO time_entries  (project_id, user_id, date, hours) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(
                con -> {
                    PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                    ps.setLong(1, timeEntry.getProjectId());
                    ps.setLong(2, timeEntry.getUserId());
                    ps.setDate(3, Date.valueOf(timeEntry.getDate()));
                    ps.setInt(4, timeEntry.getHours());
                    return ps;
                }, keyHolder
        );

        Number key = keyHolder.getKey();
        timeEntry.setId(key.longValue());
        return timeEntry;
    }

    @Override
    public TimeEntry find(long timeEntryId) {
        try {
            return jdbcTemplate.queryForObject("Select * from time_entries where id = ?", new BeanPropertyRowMapper<>(TimeEntry.class), timeEntryId);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<TimeEntry> list() {
        return jdbcTemplate.query("Select * from time_entries", new BeanPropertyRowMapper<>(TimeEntry.class));
    }

    @Override
    public TimeEntry update(long id, TimeEntry timeEntry) {
        String query = "UPDATE time_entries SET project_id = ?, user_id = ?, date = ?, hours = ? WHERE id = ?";
        jdbcTemplate.update(query,timeEntry.getProjectId(),timeEntry.getUserId(),Date.valueOf(timeEntry.getDate()),timeEntry.getHours(), id);
        return find(id);
    }

    @Override
    public void delete(long timeEntryId) {
        String query = "DELETE FROM time_entries WHERE id = ?";
        jdbcTemplate.update(query,timeEntryId);

    }
}
