package com.otel.reservation.service;

import com.otel.reservation.dbservice.GuestDbService;
import com.otel.reservation.dbservice.ReservationDbService;
import com.otel.reservation.dbservice.RoomDbService;
import com.otel.reservation.exceptions.GuestNotFoundException;
import com.otel.reservation.exceptions.ReservationNotPossibleException;
import com.otel.reservation.exceptions.RoomNotFoundException;
import com.otel.reservation.repository.entity.Reservation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
public class ReservationService {
    private final ReservationDbService reservationDbService;
    private final GuestDbService guestDbService;
    private final RoomDbService roomDbService;

    public ReservationService(ReservationDbService reservationDbService,
                              GuestDbService guestDbService,
                              RoomDbService roomDbService) {
        this.reservationDbService = reservationDbService;
        this.guestDbService = guestDbService;
        this.roomDbService = roomDbService;
    }

    public Reservation getReservation(Long id) {
        return reservationDbService.getReservationById(id);
    }

    public List<Reservation> getAllReservations() {
        return reservationDbService.getAllReservations();
    }

    public void deleteReservation(Long id) {
        reservationDbService.deleteReservation(id);
    }

    public List<Reservation> getReservationsByRoomId(Long roomId) {
        return reservationDbService.getReservationsByRoomId(roomId);
    }

    public void createReservation(
            Long guestId,
            Long roomId,
            String checkIn,
            String checkOut,
            Integer numberOfPeople) {
        var checkInDate = LocalDate.parse(checkIn);
        var checkOutDate = LocalDate.parse(checkOut);

        var guest = guestDbService.findById(guestId).orElseThrow(() -> new GuestNotFoundException(guestId));
        var room = roomDbService.findByRoomId(roomId).orElseThrow(() -> new RoomNotFoundException(roomId));


        if (Boolean.FALSE.equals(reservationDbService.isRoomAvailable(room.getId(), null, checkInDate, checkOutDate))) {
            throw new ReservationNotPossibleException("Room is not available for the selected dates.");
        }

        if (numberOfPeople > room.getCapacity()) {
            throw new ReservationNotPossibleException("Number of people exceeds room capacity.");
        }

        reservationDbService.createReservation(
                guest,
                room,
                checkInDate,
                checkOutDate,
                numberOfPeople,
                calculateTotalPrice(checkInDate, checkOutDate, room.getPricePerNight(), numberOfPeople));
    }

    public void updateReservation(
            Long reservationId,
            Long guestId,
            Long roomId,
            String checkIn,
            String checkOut,
            Integer numberOfPeople) {
        var reservation = reservationDbService.getReservationById(reservationId);
        var checkInDate = LocalDate.parse(checkIn);
        var checkOutDate = LocalDate.parse(checkOut);

        if (Boolean.FALSE.equals(reservationDbService.isRoomAvailable(reservation.getRoom().getId(), reservationId, checkInDate, checkOutDate))) {
            throw new ReservationNotPossibleException("Room is not available for the selected dates.");
        }

        if (numberOfPeople > reservation.getRoom().getCapacity()) {
            throw new ReservationNotPossibleException("Number of people exceeds room capacity.");
        }

        var guest = guestDbService.findById(guestId).orElseThrow(() -> new GuestNotFoundException(guestId));
        var room = roomDbService.findByRoomId(roomId).orElseThrow(() -> new RoomNotFoundException(roomId));

        reservationDbService.updateReservation(
                reservationId,
                guest,
                room,
                checkInDate,
                checkOutDate,
                numberOfPeople,
                calculateTotalPrice(checkInDate, checkOutDate, room.getPricePerNight(), numberOfPeople));
    }

    private static Double calculateTotalPrice(LocalDate checkIn, LocalDate checkOut, Double pricePerNight, Integer numberOfPeople) {
        var price = BigDecimal.valueOf(pricePerNight);
        long days = checkOut.toEpochDay() - checkIn.toEpochDay();
        var total = price
                .multiply(BigDecimal.valueOf(days))
                .multiply(BigDecimal.valueOf(numberOfPeople))
                .setScale(2, RoundingMode.HALF_UP);
        return total.doubleValue();
    }
}
