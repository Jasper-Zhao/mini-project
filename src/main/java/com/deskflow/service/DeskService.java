package com.deskflow.service;

import com.deskflow.exception.BadRequestException;
import com.deskflow.exception.NotFoundException;
import com.deskflow.model.Desk;
import com.deskflow.repository.DeskRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class DeskService {
    private final DeskRepository deskRepository;

    public DeskService(DeskRepository deskRepository) {
        this.deskRepository = deskRepository;
    }

    public List<Desk> list(Integer floor, Boolean hasMonitor) {
        return deskRepository.findAll(floor, hasMonitor);
    }

    public Desk getRequired(long id) {
        return deskRepository.findById(id).orElseThrow(() -> new NotFoundException("Desk " + id + " was not found"));
    }

    public List<Desk> available(LocalDate date, Integer floor, Boolean hasMonitor) {
        if (date == null) {
            throw new BadRequestException("date is required");
        }
        return deskRepository.findAvailableOn(date, floor, hasMonitor);
    }
}
