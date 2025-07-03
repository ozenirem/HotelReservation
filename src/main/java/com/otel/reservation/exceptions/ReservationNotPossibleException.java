package com.otel.reservation.exceptions;

public class ReservationNotPossibleException extends RuntimeException {
    public ReservationNotPossibleException(String message) {
        super(message);
    }
}
