package com.otel.reservation.repository;

import com.otel.reservation.repository.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByGuestId(Long guestId);
    List<Reservation> findByRoomId(Long roomId);

    List<Reservation> findByRoomIdAndIdNotAndCheckOutDateAfterAndCheckInDateBefore(
            Long roomId, Long excludeReservationId, LocalDate checkIn, LocalDate checkOut);
}
