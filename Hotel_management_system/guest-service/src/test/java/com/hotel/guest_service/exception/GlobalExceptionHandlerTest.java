package com.hotel.guest_service.exception;


import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.*;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void testHandleGuestNotFound() {
        GuestNotFoundException ex = new GuestNotFoundException("Guest not found with ID: 99");

        ResponseEntity<String> response = handler.handleGuestNotFound(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Guest not found with ID: 99", response.getBody());
    }

    @Test
    void testHandleValidationErrors() {
        // Mock FieldError
        FieldError fieldError = new FieldError("guestDTO", "phoneNumber", "Phone number must be 10 digits");

        // Mock BindingResult
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.getAllErrors()).thenReturn(List.of(fieldError));

        // Mock MethodArgumentNotValidException
        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(null, bindingResult);

        ResponseEntity<Map<String, String>> response = handler.handleValidationErrors(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().containsKey("phoneNumber"));
        assertEquals("Phone number must be 10 digits", response.getBody().get("phoneNumber"));
    }
}
