package com.hotel.reservation.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name = "room-service")
public interface RoomServiceClient {
    @GetMapping("/rooms/availability/{roomId}")
    Boolean isRoomAvailable(@PathVariable("roomId") int roomId);

    @GetMapping("/rooms/get-price/{roomNumber}")
    double getPriceByRoomId(@PathVariable("roomNumber")int roomNumber);

    @PutMapping("/rooms/updateRoom/{id}")
    public void updateRoomById(@PathVariable("id") int id);

    @PutMapping("/rooms/updateRoomToAvailabe/{id}")
    public void updateRoomToAvailableById(@PathVariable("id") int id);
}
