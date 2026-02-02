package com.hotel.room.controller;

import com.hotel.room.model.Room;
import com.hotel.room.service.RoomService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/rooms")
public class RoomController {

    @Autowired
    private RoomService roomService;

   // @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/getAll")
    public List<Room> getAllRooms() {
        return roomService.getAllRooms();
    }

    @GetMapping("/get/{roomNumber}")
    public Room getRoomByNumber(@PathVariable int roomNumber) {
        return roomService.getRoomByNumber(roomNumber);
    }

    @PostMapping("/addRoom")
    public Room addRoom(@Valid @RequestBody Room room) {
        return roomService.addRoom(room);
    }

    @DeleteMapping("/delete/{roomNumber}")
    public String deleteRoom(@PathVariable int roomNumber) {
        roomService.deleteRoom(roomNumber);
        return "Room deleted successfully.";
    }

//    @GetMapping("/get/rate/{roomNumber}")
//    public double getRoomRate(@PathVariable int roomNumber) {
//        return roomService.getRate(roomNumber);
//    }

    @GetMapping("/get/reserved/{roomNumber}")
    public boolean isRoomReserved(@PathVariable int roomNumber) {
        return roomService.checkReservation(roomNumber);
    }

    @GetMapping("/availability/{roomId}")
    public ResponseEntity<Boolean> isRoomAvailable(@PathVariable int roomId) {
        Boolean isAvailable = roomService.isRoomAvailable(roomId);
        if (isAvailable == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(false);
        }
        return ResponseEntity.ok(isAvailable);
    }

    @PutMapping("/update")
    public Room updateRoom(@Valid @RequestBody Room room) {
        return roomService.updateRoom(room);
    }

    @GetMapping("/getRateByRoomType")
    @Operation(summary = "Get rate by room type")
    public double getPriceByRoomType(@RequestParam String roomType) {
        Double rate = roomService.getPriceForRoomType(roomType);
        return rate;//ResponseEntity.ok("Rate for room type "+roomType+" is: "+rate);
    }

    @GetMapping("/get-price/{roomNumber}")
    public double getPriceByRoomId(@PathVariable("roomNumber")int roomNumber){
        return roomService.gemType(roomNumber);
    }

    @PutMapping("/updateRoom/{id}")
    public void updateRoomById(@PathVariable("id") int id){
       roomService.updateRoomById(id);
    }

    @GetMapping("/getRoom/{roomType}")
    public Room getRoomByType(@PathVariable("roomType") String roomType){
        return roomService.getRoomByType(roomType);
    }

    @GetMapping("/available")
    public List<Room> getAvailableRooms() {
        return roomService.getAvailableRooms();
    }

    @PutMapping("/rooms/updateRoomToAvailabe/{id}")
    public void updateRoomToAvailableById(@PathVariable("id") int id){
        roomService.updateRoomToAvailableById(id);
    }

    @GetMapping("/available/byType")
    @Operation(summary = "Get available rooms by type")
    public List<Room> getAvailableRoomsByType(@RequestParam String type) {
        return roomService.getAvailableRoomsByType(type);
    }

    @GetMapping("/search/byPrice")
    public List<Room> getRoomsByPrice(@RequestParam double min, @RequestParam double max) {
        return roomService.findByPriceBetween(min, max);
    }
}
