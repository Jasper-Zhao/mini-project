package com.deskflow.repository;

import com.deskflow.model.Booking;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class BookingRepository {
    private static final String SELECT_BOOKING = """
            SELECT b.id, b.desk_id, d.code AS desk_code, b.employee_name, b.booking_date, b.created_at
            FROM booking b JOIN desk d ON d.id = b.desk_id
            """;
    private static final RowMapper<Booking> BOOKING_ROW_MAPPER = (rs, rowNum) -> new Booking(
            rs.getLong("id"), rs.getLong("desk_id"), rs.getString("desk_code"),
            rs.getString("employee_name"), rs.getObject("booking_date", LocalDate.class),
            rs.getTimestamp("created_at").toInstant());

    private final JdbcTemplate jdbcTemplate;

    public BookingRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean existsByDeskIdAndDate(long deskId, LocalDate date) {
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM booking WHERE desk_id = ? AND booking_date = ?", Integer.class, deskId, date);
        return count != null && count > 0;
    }

    public Booking create(long deskId, String employeeName, LocalDate date) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO booking (desk_id, employee_name, booking_date, created_at) VALUES (?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            statement.setLong(1, deskId);
            statement.setString(2, employeeName);
            statement.setObject(3, date);
            statement.setObject(4, Instant.now());
            return statement;
        }, keyHolder);
        Number id = keyHolder.getKey();
        if (id == null) {
            throw new IllegalStateException("Booking was created without an ID");
        }
        return findById(id.longValue()).orElseThrow(() -> new IllegalStateException("Booking was not found after creation"));
    }

    public List<Booking> findByDate(LocalDate date) {
        return jdbcTemplate.query(SELECT_BOOKING + " WHERE b.booking_date = ? ORDER BY d.floor, d.code", BOOKING_ROW_MAPPER, date);
    }

    public Optional<Booking> findById(long id) {
        List<Booking> bookings = jdbcTemplate.query(SELECT_BOOKING + " WHERE b.id = ?", BOOKING_ROW_MAPPER, id);
        return bookings.stream().findFirst();
    }

    public boolean deleteById(long id) {
        return jdbcTemplate.update("DELETE FROM booking WHERE id = ?", id) > 0;
    }
}
