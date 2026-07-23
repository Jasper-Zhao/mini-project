package com.deskflow.desk;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/desks")
public class DeskController {
    private final DeskService deskService;

    public DeskController(DeskService deskService) {
        this.deskService = deskService;
    }

    @GetMapping
    public List<Desk> list(
            @RequestParam(required = false) Integer floor,
            @RequestParam(required = false) Boolean hasMonitor) {
        return deskService.list(floor, hasMonitor);
    }

    @GetMapping("/availability")
    public List<Desk> availability(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) Integer floor,
            @RequestParam(required = false) Boolean hasMonitor) {
        return deskService.available(date, floor, hasMonitor);
    }
}
