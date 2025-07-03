package com.otel.reservation.repository.entity;

import com.otel.reservation.constants.RoomType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

@Entity
@Data
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String roomNumber;

    @JdbcType(PostgreSQLEnumJdbcType.class)
    private RoomType roomType;

    private String description;

    private Integer capacity;

    private Double pricePerNight;
}
