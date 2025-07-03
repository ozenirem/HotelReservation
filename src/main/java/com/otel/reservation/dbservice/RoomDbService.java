package com.otel.reservation.dbservice;

import com.otel.reservation.constants.RoomType;
import com.otel.reservation.exceptions.RoomNotFoundException;
import com.otel.reservation.repository.RoomRepository;
import com.otel.reservation.repository.entity.Room;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class RoomDbService {

    private final RoomRepository roomRepository;

    public RoomDbService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Transactional
    public void addRoom(String roomNumber, RoomType roomType, String description, Integer capacity, Double pricePerNight) {
        var room = new Room();
        room.setRoomNumber(roomNumber);
        room.setRoomType(roomType);
        room.setDescription(description);
        room.setCapacity(capacity);
        room.setPricePerNight(pricePerNight);
        roomRepository.save(room);
    }

    @Transactional
    public Room updateRoom(String roomNumber, RoomType roomType, String description, Integer capacity, Double pricePerNight) {
        var room = findByRoomNumber(roomNumber)
                .orElseThrow(() -> new RoomNotFoundException(roomNumber));
        room.setRoomNumber(roomNumber);
        room.setRoomType(roomType);
        room.setDescription(description);
        room.setCapacity(capacity);
        room.setPricePerNight(pricePerNight);
        return roomRepository.save(room);
    }

    public Optional<Room> findByRoomId(Long id) {
        return roomRepository.findById(id);
    }

    public Optional<Room> findByRoomNumber(String roomNumber) {
        return roomRepository.findByRoomNumber(roomNumber);
    }

    public Long findIdByRoomNumber(String roomNumber) {
        return roomRepository.findByRoomNumber(roomNumber)
                .map(Room::getId)
                .orElseThrow(() -> new RoomNotFoundException(roomNumber));
    }

    public List<Room> getAllRooms() {return roomRepository.findAll();}

    public void deleteRoom(Long id) {
        roomRepository.findById(id).ifPresentOrElse(
                roomRepository::delete,
                () -> log.info("Trying to delete non-existing room with id {}", id)
        );
    }
}
