package com.hotel.guest_service.dto;

import jakarta.validation.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class GuestDTOTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidGuestDTO() {
        GuestDTO dto = new GuestDTO("M123456", "1234567890", "John Doe", "john@example.com", "Male", "123 Street");

        Set<ConstraintViolation<GuestDTO>> violations = validator.validate(dto);

        assertTrue(violations.isEmpty());
    }

    @Test
    void testBlankFields() {
        GuestDTO dto = new GuestDTO("", "", "", "", "", "");

        Set<ConstraintViolation<GuestDTO>> violations = validator.validate(dto);

        assertEquals(6, violations.size());
    }

    @Test
    void testInvalidPhoneNumber() {
        GuestDTO dto = new GuestDTO("M123456", "12345", "John Doe", "john@example.com", "Male", "123 Street");

        Set<ConstraintViolation<GuestDTO>> violations = validator.validate(dto);

        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("phoneNumber")));
    }

    @Test
    void testInvalidEmail() {
        GuestDTO dto = new GuestDTO("M123456", "1234567890", "John Doe", "invalid-email", "Male", "123 Street");

        Set<ConstraintViolation<GuestDTO>> violations = validator.validate(dto);

        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email")));
    }
}
