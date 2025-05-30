package com.office.reservation.service;

import com.office.reservation.business.IBusinessLogic;
import com.office.reservation.helper.ReservationHelper;
import com.office.reservation.model.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.YearMonth;

@Slf4j
@Service
public class ReservationService {

    @Autowired
    ReservationHelper reservationHelper;

    @Autowired
    IBusinessLogic businessLogicImpl;

    public Response fetchData(String monthYear) throws Exception {
        YearMonth yearMonth = reservationHelper.parseMonthYear(monthYear);
        businessLogicImpl.loadReservationsIfChanged();
        return businessLogicImpl.fetchReservationData(yearMonth);
    }
}

