package com.otel.reservation.domain;

import com.otel.reservation.repository.entity.Guest;

public record GuestRequest(
    String firstName,
    String lastName,
    String email,
    String phoneNumber
) {
    public Guest toGuestEntity() {
        var guest = new Guest();
        guest.setFirstName(firstName);
        guest.setLastName(lastName);
        guest.setEmail(email);
        guest.setPhoneNumber(phoneNumber);
        return guest;
    }
}
