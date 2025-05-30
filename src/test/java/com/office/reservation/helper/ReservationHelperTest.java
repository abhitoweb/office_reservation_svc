package com.office.reservation.helper;

import com.office.reservation.exception.InvalidCSVDataException;
import com.office.reservation.exception.InvalidDateException;
import com.office.reservation.model.Reservation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.YearMonth;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReservationHelperTest {

    private ReservationHelper reservationHelper;

    @BeforeEach
    void setUp() {
        reservationHelper = new ReservationHelper();
    }

    @Test
    void testParseMonthYear_valid() throws InvalidDateException {
        YearMonth result = reservationHelper.parseMonthYear("2024-07");
        assertEquals(YearMonth.of(2024, 7), result);
    }

    @Test
    void testParseMonthYear_invalid() {
        assertThrows(InvalidDateException.class, () ->
                reservationHelper.parseMonthYear("July-2024")
        );
    }

    @Test
    void testFetchCSVData_validFile() throws Exception {
        List<Reservation> reservations = reservationHelper.fetchCSVData("test-data.csv");
        assertNotNull(reservations);
        assertFalse(reservations.isEmpty());

        Reservation first = reservations.get(0);
        assertEquals(10, first.getCapacity());
        assertEquals(1000.0, first.getMonthlyPrice());
        assertEquals("2024-06-01", first.getStartDate().toString());
        assertEquals("2024-06-30", first.getEndDate().toString());
    }

    @Test
    void testFetchCSVData_invalidFile() {
        assertThrows(InvalidCSVDataException.class, () ->
                reservationHelper.fetchCSVData("non-existent.csv")
        );
    }

    @Test
    void testComputeChecksumFromClasspath_validFile() throws Exception {
        byte[] checksum = reservationHelper.computeChecksumFromClasspath("test-data.csv");

        assertNotNull(checksum);
        assertEquals(32, checksum.length); // SHA-256 produces 32 bytes
    }

    @Test
    void testComputeChecksumFromClasspath_invalidFile() {
        assertThrows(InvalidCSVDataException.class, () ->
                reservationHelper.computeChecksumFromClasspath("missing.csv")
        );
    }
}
