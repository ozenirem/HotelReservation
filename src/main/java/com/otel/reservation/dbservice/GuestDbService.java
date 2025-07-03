package com.otel.reservation.dbservice;

import com.otel.reservation.exceptions.GuestNotFoundException;
import com.otel.reservation.repository.GuestRepository;
import com.otel.reservation.repository.entity.Guest;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GuestDbService {

    private final GuestRepository guestRepository;

    public GuestDbService(GuestRepository guestRepository) {
        this.guestRepository = guestRepository;
    }

    @Transactional
    public void addGuest(String firstName, String lastName, String email, String phoneNumber) {
        var guest = new Guest();
        guest.setFirstName(firstName);
        guest.setLastName(lastName);
        guest.setEmail(email);
        guest.setPhoneNumber(phoneNumber);
        guestRepository.save(guest);
    }

    @Transactional
    public void updateGuest(String firstName, String lastName, String email, String phoneNumber) {
        var guest = findByEmail(email).orElseThrow(() -> new GuestNotFoundException(email));
        guest.setFirstName(firstName);
        guest.setLastName(lastName);
        guest.setEmail(email);
        guest.setPhoneNumber(phoneNumber);
        guestRepository.save(guest);
    }

    public void deleteGuest(Long id) {
        var guest = findById(id).orElseThrow(() -> new GuestNotFoundException(id));
        guestRepository.delete(guest);
    }

    public Optional<Guest> findById(Long id) {
        return guestRepository.findById(id);
    }

    public Optional<Guest> findByEmail(String email) {
        return guestRepository.findByEmail(email);
    }

    public List<Guest> getAllGuests() {
        return guestRepository.findAll();
    }
}
