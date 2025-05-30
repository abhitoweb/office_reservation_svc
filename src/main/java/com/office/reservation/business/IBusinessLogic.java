package com.office.reservation.business;

import com.office.reservation.exception.ReservationException;
import com.office.reservation.model.Response;

import java.time.YearMonth;

// Defined a sealed interface
public interface IBusinessLogic{
    void loadReservationsIfChanged() throws Exception;
    Response fetchReservationData(YearMonth yearMonth) throws ReservationException;
}

