package com.hotel.guest_service.exception;

public class IdLessThenException extends RuntimeException {
    public IdLessThenException(String message) {
        super(message);
    }
}
