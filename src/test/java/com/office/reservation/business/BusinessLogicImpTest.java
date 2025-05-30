package com.office.reservation.business;

import com.office.reservation.config.ReservationConfig;
import com.office.reservation.exception.ReservationException;
import com.office.reservation.helper.ReservationHelper;
import com.office.reservation.model.Reservation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BusinessLogicImpTest {

    @InjectMocks
    private BusinessLogicImp businessLogicImp;

    @Mock
    private ReservationConfig reservationConfig;

    @Mock
    private ReservationHelper reservationHelper;

    private static final String CSV_PATH = "test.csv";

    private Reservation res1, res2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Sample reservations
        res1 = new Reservation();
        res1.setCapacity(5);
        res1.setMonthlyPrice(1000);
        res1.setStartDate(LocalDate.of(2025, 5, 1));
        res1.setEndDate(LocalDate.of(2025, 5, 31));

        res2 = new Reservation();
        res2.setCapacity(3);
        res2.setMonthlyPrice(1500);
        res2.setStartDate(LocalDate.of(2025, 4, 20));
        res2.setEndDate(LocalDate.of(2025, 5, 10));

        // Inject static list
        BusinessLogicImp.reservations = Arrays.asList(res1, res2);
    }

    @Test
    void testCalculateRevenue_Success() throws ReservationException {
        YearMonth ym = YearMonth.of(2025, 5);
        double revenue = businessLogicImp.calculateRevenue(ym);

        // Expected revenue:
        // res1: full month (1000)
        // res2: 10/31 days of 1500 = approx 483.87
        assertEquals(1000 + (1500.0 * 10 / 31), revenue, 0.01);
    }

    @Test
    void testCalculateRevenue_EmptyReservations() throws ReservationException {
        BusinessLogicImp.reservations = List.of();
        double revenue = businessLogicImp.calculateRevenue(YearMonth.of(2025, 5));
        assertEquals(0.0, revenue);
    }

    @Test
    void testCalculateUnreservedCapacity() throws ReservationException {
        YearMonth ym = YearMonth.of(2025, 6); // No overlap
        int capacity = businessLogicImp.calculateUnreservedCapacity(ym);

        // Both reservations are outside June, so total unreserved capacity = 5 + 3
        assertEquals(8, capacity);
    }

    @Test
    void testLoadReservationsIfChanged_InitialLoad() throws Exception {
        byte[] checksum = new byte[]{1, 2, 3};

        when(reservationConfig.getReservationFilePath()).thenReturn(CSV_PATH);
        when(reservationHelper.computeChecksumFromClasspath(CSV_PATH)).thenReturn(checksum);
        when(reservationHelper.fetchCSVData(CSV_PATH)).thenReturn(List.of(res1, res2));

        BusinessLogicImp.storedByteStream = null;

        businessLogicImp.loadReservationsIfChanged();

        verify(reservationHelper).fetchCSVData(CSV_PATH);
        assertEquals(checksum, BusinessLogicImp.storedByteStream);
    }

    @Test
    void testLoadReservationsIfChanged_SkipWhenChecksumSame() throws Exception {
        byte[] checksum = new byte[]{1, 2, 3};
        BusinessLogicImp.storedByteStream = new byte[]{1, 2, 3};

        when(reservationConfig.getReservationFilePath()).thenReturn(CSV_PATH);
        when(reservationHelper.computeChecksumFromClasspath(CSV_PATH)).thenReturn(checksum);

        businessLogicImp.loadReservationsIfChanged();

        verify(reservationHelper, never()).fetchCSVData(anyString());
    }

    @Test
    void testCalculateRevenue_ExceptionHandling() {
        BusinessLogicImp.reservations = null;

        assertThrows(ReservationException.class,
                () -> businessLogicImp.calculateRevenue(YearMonth.of(2025, 5)));
    }

    @Test
    void testCalculateUnreservedCapacity_ExceptionHandling() {
        BusinessLogicImp.reservations = null;

        assertThrows(ReservationException.class,
                () -> businessLogicImp.calculateUnreservedCapacity(YearMonth.of(2025, 5)));
    }
}
