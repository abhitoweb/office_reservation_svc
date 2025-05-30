package com.office.reservation.controller;
//ReservationSvcController
import com.office.reservation.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reservation")
public class ReservationSvcController {

    @Autowired
    ReservationService service;

    @GetMapping("/summary")
        public ReservationService.Result getSummary(@RequestParam String month) {
        return service.calculateForMonth(month);
    }
}
