package com.deskflow.service;

import com.deskflow.exception.BadRequestException;
import com.deskflow.exception.ConflictException;
import com.deskflow.model.CreateBookingRequest;
import com.deskflow.model.Desk;
import com.deskflow.repository.BookingRepository;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BookingServiceTest {
    private final BookingRepository bookingRepository = mock(BookingRepository.class);
    private final DeskService deskService = mock(DeskService.class);
    private final BookingService bookingService = new BookingService(bookingRepository, deskService);

    @Test
    void rejectsAnInactiveDesk() {
        when(deskService.getRequired(6L)).thenReturn(new Desk(6, "HEL-3F-03", 3, true, false));

        assertThrows(BadRequestException.class, () -> bookingService.create(request(6L)));
    }

    @Test
    void rejectsDuplicateBooking() {
        when(deskService.getRequired(1L)).thenReturn(new Desk(1, "HEL-2F-01", 2, true, true));
        when(bookingRepository.existsByDeskIdAndDate(1L, LocalDate.of(2026, 7, 24))).thenReturn(true);

        assertThrows(ConflictException.class, () -> bookingService.create(request(1L)));
    }

    private CreateBookingRequest request(long deskId) {
        return new CreateBookingRequest(deskId, "Anna", LocalDate.of(2026, 7, 24));
    }
}
