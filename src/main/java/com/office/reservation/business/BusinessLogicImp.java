package com.office.reservation.business;

import com.office.reservation.config.ReservationConfig;
import com.office.reservation.exception.ReservationException;
import com.office.reservation.helper.ReservationHelper;
import com.office.reservation.model.Reservation;
import com.office.reservation.model.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
public class BusinessLogicImp implements IBusinessLogic {

    @Autowired
    ReservationConfig reservationConfig;

    @Autowired
    ReservationHelper reservationHelper;

    static List<Reservation> reservations;
    static byte[] storedByteStream;

    @Override
    public Response fetchReservationData(YearMonth yearMonth) throws ReservationException {
        try {
            LocalDate inputStart = yearMonth.atDay(1);
            LocalDate inputEnd = yearMonth.atEndOfMonth();
            double revenue = 0;
            int unreservedCapacity = 0;
            for (Reservation data : reservations) {
                boolean isTimeInRange = !(data.getStartDate().isAfter(inputEnd) ||
                        (data.getEndDate() != null && data.getEndDate().isBefore(inputStart)));

                if (isTimeInRange) {
                    //reserved
                    LocalDate resStart = data.getStartDate().isBefore(inputStart) ? inputStart : data.getStartDate();
                    LocalDate resEnd = (data.getEndDate() == null || data.getEndDate().isAfter(inputEnd)) ? inputEnd : data.getEndDate();
                    long daysReserved = resEnd.toEpochDay() - resStart.toEpochDay() + 1;
                    long totalDaysInMonth = inputEnd.toEpochDay() - inputStart.toEpochDay() + 1;
                    revenue += data.getMonthlyPrice() * ((double) daysReserved / totalDaysInMonth);
                } else {
                    //available
                    unreservedCapacity += data.getCapacity();
                }
            }

            return Response.builder().month(yearMonth.toString()).revenue(Math.round(revenue)).unreservedCapacity(unreservedCapacity).build();
        } catch (Exception e) {
            throw new ReservationException("Error while processing the reservation");
        }
    }


    @Override
    public void loadReservationsIfChanged() throws Exception {
        byte[] byteStream = reservationHelper.computeChecksumFromClasspath(reservationConfig.getReservationFilePath());
        if (storedByteStream == null || !Arrays.equals(storedByteStream, byteStream)) {
            //Load data only if a CSV never loaded or has changed, skip otherwise
            reservations = reservationHelper.fetchCSVData(reservationConfig.getReservationFilePath());
            log.info("Data fetched from CSV file");
            storedByteStream = byteStream;
        }
    }
}
