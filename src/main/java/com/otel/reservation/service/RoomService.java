package com.otel.reservation.service;

import com.otel.reservation.dbservice.RoomDbService;
import com.otel.reservation.exceptions.RoomNotFoundException;
import com.otel.reservation.repository.entity.Room;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService {

    private final RoomDbService roomDbService;

    public RoomService(RoomDbService roomDbService) {
        this.roomDbService = roomDbService;
    }

    public Room getRoom(Long id) {
        return roomDbService.findByRoomId(id).orElseThrow(() -> new RoomNotFoundException(id));
    }

    public List<Room> getAllRooms() {
        return roomDbService.getAllRooms();
    }

    public void addRoom(Room room) {
        roomDbService.addRoom(room.getRoomNumber(), room.getRoomType(), room.getDescription(),
                room.getCapacity(), room.getPricePerNight());
    }

    public Room updateRoom(Room room) {
        return roomDbService.updateRoom(room.getRoomNumber(), room.getRoomType(), room.getDescription(),
                room.getCapacity(), room.getPricePerNight());
    }

    public void deleteRoom(Long id) {
        roomDbService.deleteRoom(id);
    }
}
