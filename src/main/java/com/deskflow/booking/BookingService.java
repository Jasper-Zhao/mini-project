package com.deskflow.booking;

import com.deskflow.common.BadRequestException;
import com.deskflow.common.ConflictException;
import com.deskflow.common.NotFoundException;
import com.deskflow.desk.Desk;
import com.deskflow.desk.DeskService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class BookingService {
    private final BookingRepository bookingRepository;
    private final DeskService deskService;

    public BookingService(BookingRepository bookingRepository, DeskService deskService) {
        this.bookingRepository = bookingRepository;
        this.deskService = deskService;
    }

    public Booking create(CreateBookingRequest request) {
        Desk desk = deskService.getRequired(request.deskId());
        if (!desk.active()) {
            throw new BadRequestException("Desk " + desk.code() + " is inactive and cannot be booked");
        }
        if (bookingRepository.existsByDeskIdAndDate(desk.id(), request.date())) {
            throw new ConflictException("Desk " + desk.code() + " is already booked on " + request.date());
        }
        try {
            return bookingRepository.create(desk.id(), request.employeeName().trim(), request.date());
        } catch (DataIntegrityViolationException exception) {
            throw new ConflictException("Desk " + desk.code() + " is already booked on " + request.date());
        }
    }

    public List<Booking> listForDate(LocalDate date) {
        if (date == null) {
            throw new BadRequestException("date is required");
        }
        return bookingRepository.findByDate(date);
    }

    public void cancel(long id) {
        if (!bookingRepository.deleteById(id)) {
            throw new NotFoundException("Booking " + id + " was not found");
        }
    }
}
