package com.deskflow.model;

import java.time.Instant;
import java.time.LocalDate;

public record Booking(long id, long deskId, String deskCode, String employeeName, LocalDate date, Instant createdAt) {
}
