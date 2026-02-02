package com.hotel.guest_service.service;

import com.hotel.guest_service.exception.GuestNotFoundException;
import com.hotel.guest_service.model.GuestModel;
import com.hotel.guest_service.repository.GuestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GuestServiceImplTest {

    @Mock
    private GuestRepository guestRepository;

    @InjectMocks
    private GuestServiceImpl guestService;

    private GuestModel guest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        guest = new GuestModel();
        guest.setId(1L);
        guest.setMemberCode("M123456");
        guest.setPhoneNumber("1234567890");
        guest.setName("John Doe");
        guest.setEmail("john@example.com");
        guest.setGender("Male");
        guest.setAddress("123 Main St");
    }

    @Test
    void testAddGuest() {
        when(guestRepository.save(any(GuestModel.class))).thenReturn(guest);

        GuestModel result = guestService.addGuest(guest);

        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        verify(guestRepository, times(1)).save(any(GuestModel.class));
    }

    @Test
    void testDeleteGuestByMemberCode() {
        when(guestRepository.findByMemberCode("M123456")).thenReturn(Optional.of(guest));
        doNothing().when(guestRepository).delete(guest);

        guestService.deleteGuestByMemberCode("M123456");

        verify(guestRepository, times(1)).delete(guest);
    }

    @Test
    void testUpdateGuest() {
        GuestModel updated = new GuestModel();
        updated.setPhoneNumber("9999999999");
        updated.setName("Jane Doe");
        updated.setEmail("jane@example.com");
        updated.setGender("Female");
        updated.setAddress("456 New St");

        when(guestRepository.findById(1L)).thenReturn(Optional.of(guest));
        when(guestRepository.save(any(GuestModel.class))).thenReturn(updated);

        guestService.updateGuest(1L, updated);

        verify(guestRepository, times(1)).save(any(GuestModel.class));
        assertEquals("Jane Doe", guest.getName());
    }


    @Test
    void testGetAllGuests() {
        List<GuestModel> guestList = Arrays.asList(guest);
        when(guestRepository.findAll()).thenReturn(guestList);

        List<GuestModel> result = guestService.getAllGuests();

        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getName());
    }

    @Test
    void testGetGuestsByName() {
        List<GuestModel> guestList = Arrays.asList(guest);
        when(guestRepository.findByNameContainingIgnoreCase("John")).thenReturn(guestList);

        List<GuestModel> result = guestService.getGuestsByName("John");

        assertFalse(result.isEmpty());
        assertEquals("John Doe", result.get(0).getName());
    }

    @Test
    void testGetGuestById_NotFound() {
        when(guestRepository.findById(2L)).thenReturn(Optional.empty());

        GuestNotFoundException exception = assertThrows(GuestNotFoundException.class, () -> {
            guestService.getGuestById(2L);
        });

        assertEquals("Guest not found with ID: 2", exception.getMessage());
    }

    @Test
    void testGetGuestByMemberCode_NotFound() {
        when(guestRepository.findByMemberCode("XYZ123")).thenReturn(Optional.empty());

        GuestNotFoundException exception = assertThrows(GuestNotFoundException.class, () -> {
            guestService.getGuestByMemberCode("XYZ123");
        });

        assertEquals("Guest not found with member code: XYZ123", exception.getMessage());
    }

    @Test
    void testDeleteGuestById_NotFound() {
        when(guestRepository.existsById(99L)).thenReturn(false);

        GuestNotFoundException exception = assertThrows(GuestNotFoundException.class, () -> {
            guestService.deleteGuestById(99L);
        });

        assertEquals("Guest not found with ID: 99", exception.getMessage());
    }

    @Test
    void testGenerateSixDigitId_Length() {
        String code = GuestServiceImpl.generateSixDigitId();
        assertEquals(6, code.length());
        assertTrue(code.matches("\\d{6}"));
    }
}