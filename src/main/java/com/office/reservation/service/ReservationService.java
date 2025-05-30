package com.office.reservation.service;

import com.office.reservation.business.IBusinessLogic;
import com.office.reservation.helper.ReservationHelper;
import com.office.reservation.model.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.YearMonth;

import static com.office.reservation.constant.ReservationConstant.REVENUE;
import static com.office.reservation.constant.ReservationConstant.UNRESERVED_CAPACITY;

@Slf4j
@Service
public class ReservationService {

    @Autowired
    ReservationHelper reservationHelper;

    @Autowired
    IBusinessLogic businessLogicImpl;

    public Response fetchData(String monthYear, String task) throws Exception {
        YearMonth yearMonth = reservationHelper.parseMonthYear(monthYear);
        businessLogicImpl.loadReservationsIfChanged();
        Response response = null;
        switch(task) {
            case REVENUE:{
                double revenue = businessLogicImpl.calculateRevenue(yearMonth);
                response = Response.builder().month(yearMonth.toString()).revenue(Math.round(revenue)) .build();
                            break;
            }
            case UNRESERVED_CAPACITY: {
                int unreservedCapacity = businessLogicImpl.calculateUnreservedCapacity(yearMonth);
                response = Response.builder().month(yearMonth.toString()).unreservedCapacity(unreservedCapacity).build();
                break;
            }
        }
        return response;
    }
}

