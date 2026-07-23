package com.deskflow.repository;

import com.deskflow.model.Desk;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class DeskRepository {
    private static final RowMapper<Desk> DESK_ROW_MAPPER = (rs, rowNum) -> new Desk(
            rs.getLong("id"), rs.getString("code"), rs.getInt("floor"),
            rs.getBoolean("has_monitor"), rs.getBoolean("is_active"));

    private final JdbcTemplate jdbcTemplate;

    public DeskRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Desk> findAll(Integer floor, Boolean hasMonitor) {
        StringBuilder sql = new StringBuilder("SELECT id, code, floor, has_monitor, is_active FROM desk WHERE 1 = 1");
        List<Object> parameters = new ArrayList<>();
        appendFilters(sql, parameters, floor, hasMonitor);
        sql.append(" ORDER BY floor, code");
        return jdbcTemplate.query(sql.toString(), DESK_ROW_MAPPER, parameters.toArray());
    }

    public Optional<Desk> findById(long id) {
        List<Desk> desks = jdbcTemplate.query(
                "SELECT id, code, floor, has_monitor, is_active FROM desk WHERE id = ?", DESK_ROW_MAPPER, id);
        return desks.stream().findFirst();
    }

    public List<Desk> findAvailableOn(java.time.LocalDate date, Integer floor, Boolean hasMonitor) {
        StringBuilder sql = new StringBuilder("""
                SELECT d.id, d.code, d.floor, d.has_monitor, d.is_active
                FROM desk d
                WHERE d.is_active = TRUE
                  AND NOT EXISTS (SELECT 1 FROM booking b WHERE b.desk_id = d.id AND b.booking_date = ?)
                """);
        List<Object> parameters = new ArrayList<>();
        parameters.add(date);
        appendFilters(sql, parameters, floor, hasMonitor);
        sql.append(" ORDER BY d.floor, d.code");
        return jdbcTemplate.query(sql.toString(), DESK_ROW_MAPPER, parameters.toArray());
    }

    private void appendFilters(StringBuilder sql, List<Object> parameters, Integer floor, Boolean hasMonitor) {
        if (floor != null) {
            sql.append(" AND floor = ?");
            parameters.add(floor);
        }
        if (hasMonitor != null) {
            sql.append(" AND has_monitor = ?");
            parameters.add(hasMonitor);
        }
    }
}
