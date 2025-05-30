package com.office.reservation.service;

import com.office.reservation.business.IBusinessLogic;
import com.office.reservation.helper.ReservationHelper;
import com.office.reservation.model.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.YearMonth;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @InjectMocks
    private ReservationService reservationService;

    @Mock
    private ReservationHelper reservationHelper;

    @Mock
    private IBusinessLogic businessLogicImpl;

    private final String input = "2014-05";

    private final YearMonth parsedMonth = YearMonth.of(2014, 5);

    private final Response mockResponse = Response.builder()
            .month("2014-05")
            .revenue(5000)
            .unreservedCapacity(10)
            .build();

    @BeforeEach
    void setup() throws Exception {
        lenient().when(reservationHelper.parseMonthYear(input)).thenReturn(parsedMonth);
        lenient().when(businessLogicImpl.fetchReservationData(parsedMonth)).thenReturn(mockResponse);
    }

    @Test
    void testFetchData_success() throws Exception {
        Response response = reservationService.fetchData(input);

        assertNotNull(response);
        assertEquals("2014-05", response.getMonth());
        assertEquals(5000, response.getRevenue());
        assertEquals(10, response.getUnreservedCapacity());

        verify(reservationHelper).parseMonthYear(input);
        verify(businessLogicImpl).loadReservationsIfChanged();
        verify(businessLogicImpl).fetchReservationData(parsedMonth);
    }

    @Test
    void testFetchData_exceptionFromBusinessLogic() throws Exception {
        doThrow(new RuntimeException("Load failed")).when(businessLogicImpl).loadReservationsIfChanged();

        Exception exception = assertThrows(RuntimeException.class, () -> reservationService.fetchData(input));
        assertEquals("Load failed", exception.getMessage());

        verify(reservationHelper).parseMonthYear(input);
        verify(businessLogicImpl).loadReservationsIfChanged();
        verify(businessLogicImpl, never()).fetchReservationData(any());
    }
}
