package com.office.reservation.controller;

import com.office.reservation.model.Response;
import com.office.reservation.service.ReservationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ReservationSvcControllerTest {

    @Mock
    private ReservationService reservationService;

    @InjectMocks
    private ReservationSvcController controller;

    @Test
    void testCalculateRevenue_success() throws Exception {
        // Arrange
        String inputMonth = "2014-05";
        Response mockResponse = Response.builder()
                .month("2014-05")
                .revenue(6000)
                .unreservedCapacity(8)
                .build();

        when(reservationService.fetchData(inputMonth)).thenReturn(mockResponse);

        // Act
        Response response = controller.calculateRevenue(inputMonth);

        // Assert
        assertNotNull(response);
        assertEquals("2014-05", response.getMonth());
        assertEquals(6000, response.getRevenue());
        assertEquals(8, response.getUnreservedCapacity());

        verify(reservationService, times(1)).fetchData(inputMonth);
    }

    @Test
    void testCalculateRevenue_exceptionThrown() throws Exception {
        // Arrange
        String inputMonth = "2014-05";
        when(reservationService.fetchData(inputMonth)).thenThrow(new RuntimeException("Service failure"));

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            controller.calculateRevenue(inputMonth);
        });

        assertEquals("Service failure", exception.getMessage());
        verify(reservationService, times(1)).fetchData(inputMonth);
    }
}
