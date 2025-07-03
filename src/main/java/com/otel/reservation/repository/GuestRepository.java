package com.otel.reservation.repository;

import com.otel.reservation.repository.entity.Guest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GuestRepository extends JpaRepository<Guest, Long> {
    Optional<Guest> findByEmail(String email);

    Boolean existsByEmail(String email);
}
