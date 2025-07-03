package com.otel.reservation.domain;

import com.otel.reservation.constants.RoomType;
import com.otel.reservation.repository.entity.Room;

public record RoomRequest(
    String roomNumber,
    String roomType,
    String description,
    Integer capacity,
    Double pricePerNight
) {
    public Room toRoomEntity() {
        var room = new Room();
        room.setRoomNumber(roomNumber);
        room.setRoomType(RoomType.valueOf(roomType));
        room.setDescription(description);
        room.setCapacity(capacity);
        room.setPricePerNight(pricePerNight);
        return room;
    }
}
