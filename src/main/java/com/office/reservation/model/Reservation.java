package com.office.reservation.model;

import lombok.Data;

import java.time.LocalDate;
@Data
public class Reservation {
    private int capacity;
    private double monthlyPrice;
    private LocalDate startDate;
    private LocalDate endDate;
}

