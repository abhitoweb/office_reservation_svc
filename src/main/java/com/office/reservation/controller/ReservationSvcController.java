package com.office.reservation.controller;

import com.office.reservation.model.Response;
import com.office.reservation.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reservation")
public class ReservationSvcController {

    @Autowired
    ReservationService service;

    @GetMapping("/metrics")
        public Response calculateRevenue(@RequestParam String month) throws Exception {
            return service.fetchData(month);
    }

}
