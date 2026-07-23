package com.deskflow.booking;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record CreateBookingRequest(
        @NotNull(message = "deskId is required") Long deskId,
        @NotBlank(message = "employeeName must not be blank") String employeeName,
        @NotNull(message = "date is required") LocalDate date
) {
}
