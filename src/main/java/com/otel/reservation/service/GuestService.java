package com.otel.reservation.service;

import com.otel.reservation.dbservice.GuestDbService;
import com.otel.reservation.exceptions.GuestNotFoundException;
import com.otel.reservation.repository.entity.Guest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GuestService {
    private final GuestDbService guestDbService;

    public GuestService(GuestDbService guestDbService) {
        this.guestDbService = guestDbService;
    }

    public Guest getGuest(Long id) {
        return guestDbService.findById(id)
                .orElseThrow(() -> new GuestNotFoundException(id));
    }

    public List<Guest> getAllGuests() {return guestDbService.getAllGuests();}

    public void addGuest(Guest guest) {
        guestDbService.addGuest(guest.getFirstName(), guest.getLastName(), guest.getEmail(), guest.getPhoneNumber());
    }

    public void updateGuest(Guest guest) {
        guestDbService.updateGuest(guest.getFirstName(), guest.getLastName(), guest.getEmail(), guest.getPhoneNumber());
    }

    public void deleteGuest(Long id) {
        guestDbService.deleteGuest(id);
    }

    public Long findByEmail(String email) {
        return guestDbService.findByEmail(email)
                .map(Guest::getId)
                .orElseThrow(() -> new GuestNotFoundException(email));
    }
}
