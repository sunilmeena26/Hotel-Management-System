package com.hotel.room.service;
import com.hotel.room.client.ReservationClient;
import com.hotel.room.model.Room;
import com.hotel.room.repository.RoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RoomServiceTest {

    @Mock
    private RoomRepository roomRepository;



    @Mock
    private ReservationClient reservationClient;

    @InjectMocks
    private RoomService roomService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllRooms() {
        List<Room> rooms = Arrays.asList(new Room(), new Room());
        when(roomRepository.findAll()).thenReturn(rooms);

        List<Room> result = roomService.getAllRooms();

        assertEquals(2, result.size());
    }

    @Test
    public void testGetRoomByNumber_Found() {
        Room room = new Room();
        room.setRoomNumber(101);
        when(roomRepository.findById(101)).thenReturn(Optional.of(room));


        Room result = roomService.getRoomByNumber(101);

        assertNotNull(result);
        assertEquals(101, result.getRoomNumber());
    }

    @Test
    public void testGetRoomByNumber_NotFound() {
        when(roomRepository.findById(102)).thenReturn(Optional.empty());

        Room result = roomService.getRoomByNumber(102);

        assertNull(result);
    }

    @Test
    public void testAddRoom() {
        Room room = new Room();
        when(roomRepository.save(room)).thenReturn(room);

        Room result = roomService.addRoom(room);

        assertNotNull(result);
    }

    @Test
    public void testDeleteRoom() {
        doNothing().when(roomRepository).deleteById(103);

        roomService.deleteRoom(103);

        verify(roomRepository, times(1)).deleteById(103);
    }


    @Test
    public void testCheckReservation() {
        when(reservationClient.isRoomReserved(105)).thenReturn(true);

        boolean reserved = roomService.checkReservation(105);

        assertTrue(reserved);
    }

    @Test
    public void testIsRoomAvailable_True() {
        Room room = new Room();
        room.setAvailable(true);


        when(roomRepository.findById(106)).thenReturn(Optional.of(room));

        Boolean result = roomService.isRoomAvailable(106);

        assertTrue(result);
    }

    @Test
    public void testIsRoomAvailable_False() {
        Room room = new Room();
        room.setAvailable(false);


        when(roomRepository.findById(107)).thenReturn(Optional.of(room));

        Boolean result = roomService.isRoomAvailable(107);

        assertFalse(result);
    }

    @Test
    public void testIsRoomAvailable_NotFound() {
        when(roomRepository.findById(108)).thenReturn(Optional.empty());

        Boolean result = roomService.isRoomAvailable(108);

        assertNull(result);
    }

    @Test
    public void testUpdateRoom_Success() {
        Room existingRoom = new Room();
        existingRoom.setRoomNumber(109);
        existingRoom.setAvailable(true);

        existingRoom.setPrice(1000.0);

        Room updatedRoom = new Room();
        updatedRoom.setRoomNumber(109);
        updatedRoom.setAvailable(false);

        updatedRoom.setPrice(1500.0);

        when(roomRepository.findById(109)).thenReturn(Optional.of(existingRoom));
        when(roomRepository.save(any(Room.class))).thenReturn(existingRoom);

        Room result = roomService.updateRoom(updatedRoom);

        assertNotNull(result);

        assertFalse(result.isAvailable());
        assertEquals(1500.0, result.getPrice());
    }

    @Test
    public void testUpdateRoom_NotFound() {
        Room updatedRoom = new Room();
        updatedRoom.setRoomNumber(110);

        when(roomRepository.findById(110)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            roomService.updateRoom(updatedRoom);
        });

        assertEquals("Room not found with number: 110", exception.getMessage());
    }
}
