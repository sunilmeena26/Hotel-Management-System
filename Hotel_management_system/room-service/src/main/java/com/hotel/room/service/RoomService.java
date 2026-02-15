package com.hotel.room.service;

import com.hotel.room.client.ReservationClient;
import com.hotel.room.exception.RoomNotFoundException;
import com.hotel.room.model.Room;
import com.hotel.room.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;


import java.util.List;
import java.util.Optional;

@ConditionalOnProperty(name = "db.enabled", havingValue = "true")
@Service
public class RoomService {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private ReservationClient reservationClient;

    public List<Room> getAllRooms() {

        return roomRepository.findAll();
    }

    public Room getRoomByNumber(int roomNumber) {

        return roomRepository.findById(roomNumber).orElseThrow(()->new RoomNotFoundException("Room is not present for this room number "+roomNumber));
    }

    public Room addRoom(Room room) {
        room.setAvailable(true);
        return roomRepository.save(room);
    }

    public void deleteRoom(int roomNumber) {
        Optional<Room> existingRoomOpt = roomRepository.findById(roomNumber);
        if (existingRoomOpt.isPresent()) {
            roomRepository.deleteById(roomNumber);
        } else {
            throw new RoomNotFoundException("Room not found with number: " + roomNumber);
        }

    }


    public boolean checkReservation(int roomNumber) {
        return reservationClient.isRoomReserved(roomNumber);
    }
    public Boolean isRoomAvailable(int roomId) {
        Optional<Room> roomOpt = roomRepository.findById(roomId);
        if (roomOpt.isPresent()) {
            Room room = roomOpt.get();
            return room.isAvailable() ;
        } else {
            return null;
        }
    }



    public Room updateRoom(Room updatedRoom) {
        Optional<Room> existingRoomOpt = roomRepository.findById(updatedRoom.getRoomNumber());

        if (existingRoomOpt.isPresent()) {
            Room existingRoom = existingRoomOpt.get();
            existingRoom.setAvailable(!updatedRoom.isAvailable());

            existingRoom.setPrice(updatedRoom.getPrice());
            existingRoom.setRoomType(updatedRoom.getRoomType());
            existingRoom.setRoomPath(updatedRoom.getRoomPath());
            return roomRepository.save(existingRoom);
        } else {
            throw new RoomNotFoundException("Room not found with number: " + updatedRoom.getRoomNumber());
        }
    }
    public Double getPriceForRoomType(String roomType) {
        return roomRepository.findByRoomType(roomType)
                .map(Room::getPrice)
                .orElseThrow(() -> new RoomNotFoundException("Rate not found for room type: " + roomType));
    }

    public double gemType(int roomNumber) {
        Room room=roomRepository.findById(roomNumber).orElse(null);
        return room.getPrice();

    }

    public void updateRoomById(int id) {
        Room room=roomRepository.findById(id).orElse(null);
        room.setAvailable(true);
        roomRepository.save(room);
    }

    public Room getRoomByType(String roomType) {
        return roomRepository.getRoomByType(roomType);
    }
    public List<Room> getAvailableRooms() {
        return roomRepository.findByIsAvailableTrue();
    }

    public void updateRoomToAvailableById(int id) {
        Room room=roomRepository.findById(id).orElse(null);
        room.setAvailable(true);
        roomRepository.save(room);
    }

    public List<Room> getAvailableRoomsByType(String type) {
        return roomRepository.findByRoomTypeAndIsAvailableTrue(type);
    }

    public List<Room> findByPriceBetween(double min, double max) {
        return roomRepository.findByPriceBetween(min, max);
    }
}
