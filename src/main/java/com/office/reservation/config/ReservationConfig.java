package com.office.reservation.config;

import lombok.Data;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class ReservationConfig {

    @Value("${reservation.file.path:rent_data.csv}")
    String reservationFilePath;
}
