package com.office.reservation.business;

import com.office.reservation.config.ReservationConfig;
import com.office.reservation.exception.ReservationException;
import com.office.reservation.helper.ReservationHelper;
import com.office.reservation.model.Reservation;
import com.office.reservation.model.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BusinessLogicImpTest {

    @InjectMocks
    private BusinessLogicImp businessLogic;

    @Mock
    private ReservationHelper reservationHelper;

    @Mock
    private ReservationConfig reservationConfig;

    private static final String FILE_PATH = "mock-data.csv";

    @BeforeEach
    void setup() {
        BusinessLogicImp.storedByteStream = null;
    }

    @Test
    void testFetchReservationData_success() throws Exception {
        // Set static reservations
        Reservation res1 = new Reservation(2, 1500, LocalDate.of(2012, 6, 1), LocalDate.of(2015, 7, 15));
        Reservation res2 = new Reservation(2, 1300, LocalDate.of(2012, 10, 1), null);
        Reservation res3 = new Reservation(4, 2600, LocalDate.of(2012, 6, 1), LocalDate.of(2014, 5, 31));
        Reservation res4 = new Reservation(4, 2700, LocalDate.of(2012, 7, 1), LocalDate.of(2014, 4, 30));

        BusinessLogicImp.reservations = Arrays.asList(res1, res2, res3, res4);

        YearMonth targetMonth = YearMonth.of(2013, 1);
        Response response = businessLogic.fetchReservationData(targetMonth);

        assertEquals("2013-01", response.getMonth());
        assertEquals(8100, response.getRevenue()); // 1500+1300+2600+2700
        assertEquals(0, response.getUnreservedCapacity());
    }

    @Test
    void testFetchReservationData_unreservedCapacity() throws Exception {
        Reservation res1 = new Reservation(2, 1000, LocalDate.of(2013, 2, 1), LocalDate.of(2013, 2, 28)); // starts after Jan
        BusinessLogicImp.reservations = List.of(res1);

        YearMonth targetMonth = YearMonth.of(2013, 1);
        Response response = businessLogic.fetchReservationData(targetMonth);

        assertEquals(0, response.getRevenue());
        assertEquals(2, response.getUnreservedCapacity());
    }

    @Test
    void testFetchReservationData_exception() {
        BusinessLogicImp.reservations = null; // null will cause exception

        YearMonth targetMonth = YearMonth.of(2013, 1);
        assertThrows(ReservationException.class, () -> businessLogic.fetchReservationData(targetMonth));
    }

    @Test
    void testLoadReservationsIfChanged_shouldLoad() throws Exception {
        byte[] checksum = new byte[]{1, 2, 3};
        List<Reservation> mockData = List.of(new Reservation(1, 1000, LocalDate.now(), null));

        when(reservationConfig.getReservationFilePath()).thenReturn(FILE_PATH);
        when(reservationHelper.computeChecksumFromClasspath(FILE_PATH)).thenReturn(checksum);
        when(reservationHelper.fetchCSVData(FILE_PATH)).thenReturn(mockData);

        businessLogic.loadReservationsIfChanged();

        verify(reservationHelper, times(1)).fetchCSVData(FILE_PATH);
        assertArrayEquals(checksum, BusinessLogicImp.storedByteStream);
        assertEquals(mockData, BusinessLogicImp.reservations);
    }

    @Test
    void testLoadReservationsIfChanged_shouldNotLoad() throws Exception {
        byte[] checksum = new byte[]{1, 2, 3};
        BusinessLogicImp.storedByteStream = new byte[]{1, 2, 3};

        when(reservationConfig.getReservationFilePath()).thenReturn(FILE_PATH);
        when(reservationHelper.computeChecksumFromClasspath(FILE_PATH)).thenReturn(checksum);

        businessLogic.loadReservationsIfChanged();

        verify(reservationHelper, never()).fetchCSVData(anyString());
    }
}
