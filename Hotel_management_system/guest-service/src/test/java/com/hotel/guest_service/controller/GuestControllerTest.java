package com.hotel.guest_service.controller;

import com.hotel.guest_service.model.GuestModel;
import com.hotel.guest_service.service.GuestServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GuestControllerTest {

    @Mock
    private GuestServiceImpl guestService;

    @InjectMocks
    private GuestController guestController;

    private GuestModel guest;

    @BeforeEach
    void setUp() {
        guest = new GuestModel();
        guest.setId(1L);
        guest.setMemberCode("M123");
        guest.setPhoneNumber("1234567890");
        guest.setName("John Doe");
        guest.setEmail("john@example.com");
        guest.setGender("Male");
        guest.setAddress("123 Main St");
    }


    @Test
    void testAddGuest() {
        // Mock the behavior of addGuest to return the same guest
        when(guestService.addGuest(any(GuestModel.class))).thenReturn(guest);


        ResponseEntity<String> response = guestController.addGuest(guest);

        assertEquals(200, response.getStatusCodeValue());
        assertNotEquals("Guest successfully added to the database.\n"+"Your Member code is: ", guest.getMemberCode());

        verify(guestService, times(1)).addGuest(any(GuestModel.class));
    }

    @Test
    void testGetGuestById() {
        when(guestService.getGuestById(1L)).thenReturn(guest);

        GuestModel response = guestController.getGuestById(1L);


        assertEquals("John Doe", response.getName());
    }

    @Test
    void testGetGuestByMemberCode() {
        when(guestService.getGuestByMemberCode("M123")).thenReturn(guest);

        ResponseEntity<GuestModel> response = guestController.getGuestByMemberCode("M123");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("M123", response.getBody().getMemberCode());
    }

    @Test
    void testDeleteGuestById() {
        doNothing().when(guestService).deleteGuestById(1L);

        ResponseEntity<String> response = guestController.deleteGuestById(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Guest deleted successfully.", response.getBody());
        verify(guestService, times(1)).deleteGuestById(1L);
    }

    @Test
    void testDeleteGuestByMemberCode() {
        doNothing().when(guestService).deleteGuestByMemberCode("M123");

        ResponseEntity<String> response = guestController.deleteGuestByMemberCode("M123");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Guest with member code deleted successfully.", response.getBody());
        verify(guestService, times(1)).deleteGuestByMemberCode("M123");
    }

    @Test
    void testUpdateGuest() {
        doNothing().when(guestService).updateGuest(eq(1L), any(GuestModel.class));

        ResponseEntity<String> response = guestController.updateGuest(1L, guest);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Guest updated successfully.", response.getBody());
        verify(guestService, times(1)).updateGuest(1L, guest);
    }

    @Test
    void testGetAllGuests() {
        List<GuestModel> guestList = Arrays.asList(guest);

        when(guestService.getAllGuests()).thenReturn(guestList);

        List<GuestModel> response = guestController.getAllGuests();

        assertEquals(1, response.size());
        assertEquals("John Doe", response.get(0).getName());
        verify(guestService, times(1)).getAllGuests();
    }

    @Test
    void testGetGuestsByName() {
        List<GuestModel> guestList = Arrays.asList(guest);

        when(guestService.getGuestsByName("John")).thenReturn(guestList);

        ResponseEntity<List<GuestModel>> response = guestController.getGuestsByName("John");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        assertEquals("John Doe", response.getBody().get(0).getName());
        verify(guestService, times(1)).getGuestsByName("John");
    }
}
