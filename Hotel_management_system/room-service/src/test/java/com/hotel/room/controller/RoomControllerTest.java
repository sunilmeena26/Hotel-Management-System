package com.hotel.room.controller;

import com.hotel.room.model.Room;
import com.hotel.room.service.RoomService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RoomControllerTest {

    @Mock
    private RoomService roomService;

    @InjectMocks
    private RoomController roomController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllRooms() {
        List<Room> rooms = Arrays.asList(new Room(), new Room());
        when(roomService.getAllRooms()).thenReturn(rooms); // Corrected line

        List<Room> result = roomController.getAllRooms();

        assertEquals(2, result.size());
    }


    @Test
    public void testGetRoomByNumber() {
        Room room = new Room();
        room.setRoomNumber(101);
        when(roomService.getRoomByNumber(101)).thenReturn(room);

        Room result = roomController.getRoomByNumber(101);

        assertEquals(101, result.getRoomNumber());
    }

    @Test
    public void testAddRoom() {
        Room room = new Room();
        room.setRoomNumber(102);
        when(roomService.addRoom(room)).thenReturn(room);

        Room result = roomController.addRoom(room);

        assertEquals(102, result.getRoomNumber());
    }

    @Test
    public void testDeleteRoom() {
        doNothing().when(roomService).deleteRoom(103);

        String result = roomController.deleteRoom(103);

        assertEquals("Room deleted successfully.", result);
        verify(roomService, times(1)).deleteRoom(103);
    }



    @Test
    public void testIsRoomReserved() {
        when(roomService.checkReservation(105)).thenReturn(true);

        boolean reserved = roomController.isRoomReserved(105);

        assertTrue(reserved);
    }

    @Test
    public void testIsRoomAvailable_Found() {
        when(roomService.isRoomAvailable(106)).thenReturn(true);

        ResponseEntity<Boolean> response = roomController.isRoomAvailable(106);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(Boolean.TRUE, response.getBody());
    }

    @Test
    public void testIsRoomAvailable_NotFound() {
        when(roomService.isRoomAvailable(107)).thenReturn(null);

        ResponseEntity<Boolean> response = roomController.isRoomAvailable(107);

        assertEquals(404, response.getStatusCodeValue());
        assertNotEquals(Boolean.TRUE, response.getBody());
    }

    @Test
    public void testUpdateRoom() {
        Room room = new Room();
        room.setRoomNumber(108);
        when(roomService.updateRoom(room)).thenReturn(room);

        Room result = roomController.updateRoom(room);

        assertEquals(108, result.getRoomNumber());
    }
}
