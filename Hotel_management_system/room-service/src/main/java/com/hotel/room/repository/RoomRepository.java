package com.hotel.room.repository;

import com.hotel.room.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Integer> {
    Optional<Room> findByRoomType(String roomType);

    @Query("SELECT r FROM Room r WHERE r.roomType = :roomType")
    public Room getRoomByType(@Param("roomType") String roomType);

    List<Room> findByIsAvailableTrue();

    List<Room> findByRoomTypeAndIsAvailableTrue(String roomType);

    List<Room> findByPriceBetween(double min, double max);
}
