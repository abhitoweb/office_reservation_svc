package com.office.reservation;

import com.office.reservation.model.Reservation;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
@Slf4j
public class ReservationHandler {
    public static void main(String[] args) {

        String filePath = "resources/rent_data.csv";

        ClassLoader classLoader = ReservationHandler.class.getClassLoader();
        try (InputStream inputStream = classLoader.getResourceAsStream("rent_data.csv");
             InputStreamReader streamReader = new InputStreamReader(inputStream);
             CSVReader csvReader = new CSVReader(streamReader)) {
            List<String[]> rows = csvReader.readAll();
            boolean skipHeader = true;
            List<Reservation> reservations = new ArrayList<>();
            for (String[] row : rows) {
                log.info(String.join(" | ", row));
                if(skipHeader) { // skip 1st header row
                    skipHeader = false;
                }
                else {
                    Reservation rData = getReservation(row);
                    reservations.add(rData);
                }
            }
        } catch (IOException | CsvException e) {
            log.error("Execption in loading/reading csv file is: ", e.getMessage());
        }

    }
    public static Reservation getReservation(String[] row) {
        Reservation rData = new Reservation();
        rData.setCapacity(Integer.parseInt(row[0].trim()));
        rData.setMonthlyPrice(Double.parseDouble(row[1].trim()));
        rData.setStartDate(LocalDate.parse(row[2].trim()));
        LocalDate endDate =null;
        if(row.length > 3 && !row[3].trim().isEmpty()){
            endDate = LocalDate.parse(row[3].trim());
        }
        rData.setEndDate(endDate);
        return rData;
    }

}