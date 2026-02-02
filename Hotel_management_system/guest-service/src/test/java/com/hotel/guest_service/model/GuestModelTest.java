package com.hotel.guest_service.model;

import jakarta.validation.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class GuestModelTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidGuestModel() {
        GuestModel guest = new GuestModel(
                1L,
                "M123456",
                "9876543210",
                "Alice",
                "alice@example.com",
                "Female",
                "456 Park Avenue"
        );

        Set<ConstraintViolation<GuestModel>> violations = validator.validate(guest);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testNullFields() {
        GuestModel guest = new GuestModel(
                1L,
                "M123456",
                null, // phoneNumber
                null, // name
                "alice@example.com",
                null, // gender
                null  // address
        );

        Set<ConstraintViolation<GuestModel>> violations = validator.validate(guest);
        assertEquals(4, violations.size()); // phoneNumber, name, gender, address
    }

    @Test
    void testInvalidPhoneNumber() {
        GuestModel guest = new GuestModel(
                1L,
                "M123456",
                "12345", // invalid
                "Alice",
                "alice@example.com",
                "Female",
                "456 Park Avenue"
        );

        Set<ConstraintViolation<GuestModel>> violations = validator.validate(guest);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("phoneNumber")));
    }

    @Test
    void testInvalidEmailFormat() {
        GuestModel guest = new GuestModel(
                1L,
                "M123456",
                "9876543210",
                "Alice",
                "invalid-email", // wrong format
                "Female",
                "456 Park Avenue"
        );

        Set<ConstraintViolation<GuestModel>> violations = validator.validate(guest);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email")));
    }
}
