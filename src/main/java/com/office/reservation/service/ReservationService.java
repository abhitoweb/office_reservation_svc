package com.office.reservation.service;

import com.office.reservation.config.ReservationConfig;
import com.office.reservation.helper.ReservationHelper;
import com.office.reservation.model.Reservation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Service
public class ReservationService {

    @Autowired
    ReservationConfig reservationConfig;

    @Autowired
    ReservationHelper reservationHelper;


    public Result calculateForMonth(String monthYear) {
        YearMonth ym = YearMonth.parse(monthYear);
        LocalDate inputStart = ym.atDay(1);
        LocalDate inputEnd = ym.atEndOfMonth();

        double revenue = 0;
        int unreservedCapacity = 0;
        List<Reservation> reservations = reservationHelper.fetchCSVData(reservationConfig.getReservationFilePath());

        for (Reservation data : reservations) {
            boolean isTimeInRange = !(data.getStartDate().isAfter(inputEnd) || (data.getEndDate() != null && data.getEndDate().isBefore(inputStart)));
            // true if row time period lies in input month range
            if (isTimeInRange) {

                LocalDate resStart = data.getStartDate().isBefore(inputStart) ? inputStart : data.getStartDate();
                LocalDate resEnd = (data.getEndDate() == null || data.getEndDate().isAfter(inputEnd)) ? inputEnd : data.getEndDate();
                long daysReserved = resEnd.toEpochDay() - resStart.toEpochDay() + 1;
                long totalDaysInMonth = inputEnd.toEpochDay() - inputStart.toEpochDay() + 1;
                revenue += data.getMonthlyPrice() * ((double) daysReserved / totalDaysInMonth);
            } else {
                unreservedCapacity += data.getCapacity();
            }
        }

        return new Result(ym.toString(), Math.round(revenue), unreservedCapacity);
    }

    public static class Result {
        public String month;
        public long revenue;
        public int unreservedCapacity;

        public Result(String month, long revenue, int unreservedCapacity) {
            this.month = month;
            this.revenue = revenue;
            this.unreservedCapacity = unreservedCapacity;
        }
    }
}
