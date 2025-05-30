package com.office.reservation.business;

import com.office.reservation.exception.ReservationException;

import java.time.YearMonth;

// Defined a sealed interface
public interface IBusinessLogic{
    double calculateRevenue(YearMonth yearMonth) throws ReservationException;
    int calculateUnreservedCapacity(YearMonth yearMonth) throws ReservationException;
    void loadReservationsIfChanged() throws Exception;
}

