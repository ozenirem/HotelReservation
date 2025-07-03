package com.otel.reservation.domain;


public record ReservationRequest(
    Long guestId,
    Long roomId,
    String checkInDate,
    String checkOutDate,
    Integer numberOfPeople,
    Double totalPrice
) {
}
