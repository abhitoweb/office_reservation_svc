package com.office.reservation.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
@Data
@AllArgsConstructor
@Builder
public class Reservation {
    private int capacity;
    private double monthlyPrice;
    private LocalDate startDate;
    private LocalDate endDate;
}

