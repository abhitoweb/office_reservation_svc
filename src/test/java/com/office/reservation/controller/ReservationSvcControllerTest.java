package com.office.reservation.controller;

import com.office.reservation.model.Response;
import com.office.reservation.service.ReservationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReservationSvcControllerTest {

    @Mock
    private ReservationService service;

    @InjectMocks
    private ReservationSvcController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCalculateRevenue_Success() throws Exception {
        // Arrange
        String month = "2025-05";
        Response mockResponse =  Response.builder().build();
        mockResponse.setMonth(month);
        mockResponse.setRevenue(12345);

        when(service.fetchData(month, "revenue")).thenReturn(mockResponse);

        // Act
        Response response = controller.calculateRevenue(month);

        // Assert
        assertNotNull(response);
        assertEquals(month, response.getMonth());
        assertEquals(12345, response.getRevenue());
        verify(service).fetchData(month, "revenue");
    }

    @Test
    void testCalculateUnreservedCapacity_Success() throws Exception {
        // Arrange
        String month = "2025-05";
        Response mockResponse = Response.builder().build();
        mockResponse.setMonth(month);
        mockResponse.setUnreservedCapacity(42);

        when(service.fetchData(month, "unreservedCapacity")).thenReturn(mockResponse);

        // Act
        Response result = controller.calculateUnreservedCapacity(month);

        // Assert
        assertNotNull(result);
        assertEquals(month, result.getMonth());
        assertEquals(42, result.getUnreservedCapacity());
        verify(service).fetchData(month, "unreservedCapacity");
    }

    @Test
    void testCalculateRevenue_Exception() throws Exception {
        String month = "2025-05";
        when(service.fetchData(month, "revenue")).thenThrow(new RuntimeException("Something went wrong"));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            controller.calculateRevenue(month);
        });

        assertEquals("Something went wrong", exception.getMessage());
    }

    @Test
    void testCalculateUnreservedCapacity_Exception() throws Exception {
        String month = "2025-05";
        when(service.fetchData(month, "unreservedCapacity")).thenThrow(new RuntimeException("Service failure"));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            controller.calculateUnreservedCapacity(month);
        });

        assertEquals("Service failure", exception.getMessage());
    }
}
