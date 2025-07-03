package com.otel.reservation.repository;

import com.otel.reservation.repository.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {
    Optional<Room> findByRoomNumber(String roomNumber);
    Boolean existsByRoomNumber(String roomNumber);
}
