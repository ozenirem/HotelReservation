package com.otel.reservation.web;

import com.otel.reservation.domain.GuestRequest;
import com.otel.reservation.domain.ReservationRequest;
import com.otel.reservation.domain.RoomRequest;
import com.otel.reservation.repository.entity.Guest;
import com.otel.reservation.repository.entity.Reservation;
import com.otel.reservation.repository.entity.Room;
import com.otel.reservation.service.GuestService;
import com.otel.reservation.service.ReservationService;
import com.otel.reservation.service.RoomService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("${otel.reservation.api.base-path}")
@Slf4j
public class OtelController {
    private final GuestService guestService;
    private final ReservationService reservationService;
    private final RoomService roomService;

    public OtelController(GuestService guestService, ReservationService reservationService, RoomService roomService) {
        this.guestService = guestService;
        this.reservationService = reservationService;
        this.roomService = roomService;
    }

    @PostMapping(
        value = "/room",
        consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> addRoom(@RequestBody RoomRequest roomRequest) {
        var room = roomRequest.toRoomEntity();
        log.info("Adding room {}", room.getRoomNumber());
        roomService.addRoom(room);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/room/{id}")
    public ResponseEntity<Room> getRoom(@PathVariable Long id) {
        log.info("Fetching room {}", id);
        var room = roomService.getRoom(id);
        return ResponseEntity.ok(room);
    }

    @PutMapping(value = "/room/{id}")
    public ResponseEntity<Void> updateRoom(@PathVariable Long id, @RequestBody RoomRequest roomRequest) {
        var room = roomRequest.toRoomEntity();
        log.info("Updating room {}", id);
        var roomUpdated = roomService.updateRoom(room);
        reservationService.getReservationsByRoomId(id)
                .forEach(reservation -> {
                    log.info("Updating reservation {} as room {} updated", reservation.getId(), id);
                    reservation.setRoom(roomUpdated);
                    reservationService.updateReservation(
                            reservation.getId(),
                            reservation.getGuest().getId(),
                            reservation.getRoom().getId(),
                            reservation.getCheckInDate().toString(),
                            reservation.getCheckOutDate().toString(),
                            reservation.getNumberOfPeople()
                    );
                });
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/room/{id}")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long id) {
        log.info("Deleting room {}", id);
        roomService.deleteRoom(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/rooms")
    public ResponseEntity<List<Room>> getAllRooms() {
        log.info("Fetching all rooms");
        var rooms = roomService.getAllRooms();
        return ResponseEntity.ok(rooms);
    }

    @PostMapping(
        value = "/guest",
        consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> addGuest(@RequestBody GuestRequest guestRequest) {
        var guest = guestRequest.toGuestEntity();
        log.info("Adding guest with email {} with id {}", guest.getEmail(), guest.getId());
        guestService.addGuest(guest);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/guest/{id}")
    public ResponseEntity<Guest> getGuest(@PathVariable Long id) {
        log.info("Fetching guest with id {}", id);
        var guest = guestService.getGuest(id);
        return ResponseEntity.ok(guest);
    }

    @PutMapping(value = "/guest/{id}")
    public ResponseEntity<Guest> updateGuest(@PathVariable Long id, @RequestBody GuestRequest guestRequest) {
        var guest = guestRequest.toGuestEntity();
        log.info("Updating guest {}", id);
        guestService.updateGuest(guest);
        return ResponseEntity.ok(guest);
    }

    @DeleteMapping(value = "/guest/{id}")
    public ResponseEntity<Void> deleteGuest(@PathVariable Long id) {
        log.info("Deleting guest with id {}", id);
        guestService.deleteGuest(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/guests")
    public ResponseEntity<List<Guest>> getAllGuests() {
        log.info("Fetching all guests");
        var guests = guestService.getAllGuests();
        return ResponseEntity.ok(guests);
    }

    @PostMapping(
        value = "/reservation",
        consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> addReservation(@RequestBody ReservationRequest reservationRequest) {
        log.info("Creating reservation for guest with id {} in room {}", reservationRequest.guestId(), reservationRequest.roomId());
        reservationService.createReservation(
                reservationRequest.guestId(),
                reservationRequest.roomId(),
                reservationRequest.checkInDate(),
                reservationRequest.checkOutDate(),
                reservationRequest.numberOfPeople()
        );
        return ResponseEntity.ok().build();
    }

    @PutMapping(value = "/reservation/{id}")
    public ResponseEntity<Void> updateReservation(@PathVariable Long id, @RequestBody ReservationRequest reservationRequest) {
        log.info("Updating reservation with id {}", id);
        reservationService.updateReservation(
                id,
                reservationRequest.guestId(),
                reservationRequest.roomId(),
                reservationRequest.checkInDate(),
                reservationRequest.checkOutDate(),
                reservationRequest.numberOfPeople()
        );
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/reservation/{id}")
    public ResponseEntity<Reservation> getReservation(@PathVariable Long id) {
        log.info("Fetching reservation with id {}", id);
        var reservation = reservationService.getReservation(id);
        return ResponseEntity.ok(reservation);
    }

    @DeleteMapping(value = "/reservation/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        log.info("Deleting reservation with id {}", id);
        reservationService.deleteReservation(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/reservations")
    public ResponseEntity<List<Reservation>> getAllReservations() {
        log.info("Fetching all reservations");
        var reservations = reservationService.getAllReservations();
        return ResponseEntity.ok(reservations);
    }
}