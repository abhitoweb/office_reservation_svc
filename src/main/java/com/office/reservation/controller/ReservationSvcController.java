package com.office.reservation.controller;
import com.office.reservation.model.Response;
import com.office.reservation.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.office.reservation.constant.ReservationConstant.REVENUE;
import static com.office.reservation.constant.ReservationConstant.UNRESERVED_CAPACITY;

@RestController
@RequestMapping("/reservation")
public class ReservationSvcController {

    @Autowired
    ReservationService service;

    @GetMapping("/revenue")
        public Response calculateRevenue(@RequestParam String month) throws Exception {
            return service.fetchData(month,REVENUE);
    }

    @GetMapping("/unreservedCapacity")
    public Response calculateUnreservedCapacity(@RequestParam String month) throws Exception {
        return service.fetchData(month,UNRESERVED_CAPACITY);
    }
}
