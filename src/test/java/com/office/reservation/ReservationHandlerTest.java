package com.office.reservation;

import com.office.reservation.model.Reservation;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReservationHandlerTest {

    private CSVReader csvReaderMock;

    @BeforeEach
    void setUp() {
        csvReaderMock = mock(CSVReader.class);
    }

    @Test
    void testGetReservation() {
        String[] row = {"4", "2500.50", "2025-01-01", "2025-12-31"};
        Reservation reservation = ReservationHandler.getReservation(row);

        assertNotNull(reservation);
        assertEquals(4, reservation.getCapacity());
        assertEquals(2500.50, reservation.getMonthlyPrice());
        assertEquals(LocalDate.of(2025, 1, 1), reservation.getStartDate());
        assertEquals(LocalDate.of(2025, 12, 31), reservation.getEndDate());
    }

    @Test
    void testGetReservationWithEmptyEndDate() {
        String[] row = {"3", "1800.75", "2025-05-10", ""};
        Reservation reservation = ReservationHandler.getReservation(row);

        assertNotNull(reservation);
        assertEquals(3, reservation.getCapacity());
        assertEquals(1800.75, reservation.getMonthlyPrice());
        assertEquals(LocalDate.of(2025, 5, 10), reservation.getStartDate());
        assertNull(reservation.getEndDate()); // End date should be null
    }

    @Test
    void testCsvReaderMock() throws IOException, CsvException {
        when(csvReaderMock.readAll()).thenReturn(
                Arrays.asList(
                        new String[]{"Capacity", "MonthlyPrice", "StartDate", "EndDate"},
                        new String[]{"4", "2500.50", "2025-01-01", "2025-12-31"},
                        new String[]{"3", "1800.75", "2025-05-10", ""}
                )
        );

        List<String[]> rows = csvReaderMock.readAll();
        assertEquals(3, rows.size()); // Including header row

        String[] secondRow = rows.get(1);
        assertEquals("2500.50", secondRow[1]); // Verifying data
    }
}
