package com.otel.reservation.dbservice;

import com.otel.reservation.exceptions.ReservationNotFoundException;
import com.otel.reservation.repository.ReservationRepository;
import com.otel.reservation.repository.entity.Guest;
import com.otel.reservation.repository.entity.Reservation;
import com.otel.reservation.repository.entity.Room;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReservationDbService {

    private final ReservationRepository reservationRepository;

    public ReservationDbService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @Transactional
    public void createReservation(Guest guest, Room room,
                                  LocalDate checkInDate, LocalDate checkOutDate,
                                  Integer numberOfPeople, Double totalPrice) {
        var reservation = new Reservation();
        reservation.setGuest(guest);
        reservation.setRoom(room);
        reservation.setCheckInDate(checkInDate);
        reservation.setCheckOutDate(checkOutDate);
        reservation.setNumberOfPeople(numberOfPeople);
        reservation.setTotalPrice(totalPrice);

        reservationRepository.save(reservation);
    }

    @Transactional
    public void updateReservation(Long id, Guest guest, Room room,
                                  LocalDate checkInDate, LocalDate checkOutDate,
                                  Integer numberOfPeople, Double totalPrice) {
        var reservation = getReservationById(id);
        reservation.setGuest(guest);
        reservation.setRoom(room);
        reservation.setCheckInDate(checkInDate);
        reservation.setCheckOutDate(checkOutDate);
        reservation.setNumberOfPeople(numberOfPeople);
        reservation.setTotalPrice(totalPrice);

        reservationRepository.save(reservation);
    }

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    public Reservation getReservationById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new ReservationNotFoundException(id));
    }

    public List<Reservation> getReservationsByGuestId(Long guestId) {
        return reservationRepository.findByGuestId(guestId);
    }

    public List<Reservation> getReservationsByRoomId(Long roomId) {
        return reservationRepository.findByRoomId(roomId);
    }

    public void deleteReservation(Long id) {
        if (!reservationRepository.existsById(id)) {
            throw new ReservationNotFoundException(id);
        }
        reservationRepository.deleteById(id);
    }

    public Boolean isRoomAvailable(Long roomId, Long excludeReservationId, LocalDate checkIn, LocalDate checkOut) {
        var reservations = reservationRepository.findByRoomIdAndIdNotAndCheckOutDateAfterAndCheckInDateBefore(
                roomId, excludeReservationId, checkIn, checkOut);
        return reservations.isEmpty();
    }
}
