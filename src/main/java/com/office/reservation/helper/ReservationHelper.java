package com.office.reservation.helper;

import com.office.reservation.exception.InvalidCSVDataException;
import com.office.reservation.exception.InvalidDateException;
import com.office.reservation.model.Reservation;
import com.opencsv.CSVReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
@Slf4j
@Component
public class ReservationHelper {

    public List<Reservation> fetchCSVData(String filePath) throws InvalidCSVDataException {

        List<Reservation> reservations = new ArrayList<>();
        ClassLoader classLoader = ReservationHelper.class.getClassLoader();
        try {
            InputStream inputStream = classLoader.getResourceAsStream(filePath);
            InputStreamReader streamReader = new InputStreamReader(inputStream);
            CSVReader csvReader = new CSVReader(streamReader);
            List<String[]> rows = csvReader.readAll();
            boolean skipHeader = true;
            reservations= new ArrayList<>();
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
        } catch (Exception e) {
            log.error("Error in loading/reading csv file is: {}", e.getMessage());
            throw new InvalidCSVDataException("CSV Data is invalid or not in correct format");
        }
        return reservations;

    }

    private Reservation getReservation(String[] row) {
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

    public byte[] computeChecksumFromClasspath(String resourcePath) throws Exception {
        try{
            ClassLoader classLoader = ReservationHelper.class.getClassLoader();
            InputStream inputStream = classLoader.getResourceAsStream(resourcePath);
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                digest.update(buffer, 0, bytesRead);
            }
            return digest.digest();
        } catch (Exception e) {
            log.error("Error in loading/reading csv file is: {}", e.getMessage());
            throw new InvalidCSVDataException("CSV Data is invalid or not in correct format");
        }
    }


    public YearMonth parseMonthYear(String monthYear) throws InvalidDateException {
        try {
            return YearMonth.parse(monthYear);
        } catch (Exception e) {
            throw new InvalidDateException("Invalid Month-Year passed.");
        }
    }
}
