package com.otel.reservation.exceptions;

public class GuestNotFoundException extends RuntimeException {
    public GuestNotFoundException(String email) {
        super("Guest not found with email: " + email);
    }

    public GuestNotFoundException(Long id) {
        super("Guest not found with ID: " + id);
    }
}
