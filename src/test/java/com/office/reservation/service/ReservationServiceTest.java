package com.office.reservation.service;

import com.office.reservation.business.IBusinessLogic;
import com.office.reservation.helper.ReservationHelper;
import com.office.reservation.model.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.time.YearMonth;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReservationServiceTest {

    @InjectMocks
    private ReservationService reservationService;

    @Mock
    private ReservationHelper reservationHelper;

    @Mock
    private IBusinessLogic businessLogic;

    private final String validMonthYear = "2025-06";
    private final YearMonth parsedYearMonth = YearMonth.of(2025, 6);

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFetchData_Revenue() throws Exception {
        when(reservationHelper.parseMonthYear(validMonthYear)).thenReturn(parsedYearMonth);
        doNothing().when(businessLogic).loadReservationsIfChanged();
        when(businessLogic.calculateRevenue(parsedYearMonth)).thenReturn(1234.56);

        Response response = reservationService.fetchData(validMonthYear, "revenue");

        assertNotNull(response);
        assertEquals("2025-06", response.getMonth());
        assertEquals(1235, response.getRevenue()); // Rounded from 1234.56
        assertEquals(0, response.getUnreservedCapacity()); // default value
    }

    @Test
    void testFetchData_UnreservedCapacity() throws Exception {
        when(reservationHelper.parseMonthYear(validMonthYear)).thenReturn(parsedYearMonth);
        doNothing().when(businessLogic).loadReservationsIfChanged();
        when(businessLogic.calculateUnreservedCapacity(parsedYearMonth)).thenReturn(10);

        Response response = reservationService.fetchData(validMonthYear, "unreservedCapacity");

        assertNotNull(response);
        assertEquals("2025-06", response.getMonth());
        assertEquals(10, response.getUnreservedCapacity());
        assertEquals(0, response.getRevenue()); // default value
    }

    @Test
    void testFetchData_InvalidTask_ReturnsNull() throws Exception {
        when(reservationHelper.parseMonthYear(validMonthYear)).thenReturn(parsedYearMonth);
        doNothing().when(businessLogic).loadReservationsIfChanged();

        Response response = reservationService.fetchData(validMonthYear, "invalidTask");

        assertNull(response); // switch block falls through with no match
    }

    @Test
    void testFetchData_ThrowsExceptionFromHelper() throws Exception {
        when(reservationHelper.parseMonthYear(validMonthYear)).thenThrow(new IllegalArgumentException("Invalid format"));

        assertThrows(IllegalArgumentException.class, () ->
                reservationService.fetchData(validMonthYear, "revenue")
        );
    }
}
