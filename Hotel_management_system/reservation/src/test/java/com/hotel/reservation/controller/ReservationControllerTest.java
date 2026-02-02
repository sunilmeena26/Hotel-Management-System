package com.hotel.reservation.controller;






import com.hotel.reservation.controller.ReservationController;
import com.hotel.reservation.dto.GuestDTO;
import com.hotel.reservation.dto.ReservationDTO;
import com.hotel.reservation.model.Reservation;
import com.hotel.reservation.service.ReservationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.*;

        import static org.junit.jupiter.api.Assertions.*;
        import static org.mockito.Mockito.*;

public class ReservationControllerTest {

    @Mock
    private ReservationService reservationService;

    @InjectMocks
    private ReservationController reservationController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateReservation() {
        // Arrange
        Reservation reservation = new Reservation();
        ReservationDTO mockDto = new ReservationDTO();
        mockDto.setMassageSuccessFully("Your Reservation Is SuccessFull Completed");

        when(reservationService.addReservation(reservation)).thenReturn(mockDto);

        // Act
        ResponseEntity<ReservationDTO> response = reservationController.createReservation(reservation);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("Your Reservation Is SuccessFull Completed", response.getBody().getMassageSuccessFully());
    }


    @Test
    public void testGetAllReservations() {
        List<Reservation> reservations = Arrays.asList(new Reservation(), new Reservation());
        when(reservationService.getAllReservations()).thenReturn(reservations);

        List<Reservation> result = reservationController.getAllReservations();

        assertEquals(2, result.size());
    }

    @Test
    public void testGetReservationWithDetails_Found() {
        Reservation reservation = new Reservation();
        reservation.setId(1L);
        reservation.setGuestId(101L);
        reservation.setRoomId(145);

        when(reservationService.getReservationById(1L)).thenReturn(Optional.of(reservation));
        when(reservationService.getGuestDetails(101L)).thenReturn(new GuestDTO());
        when(reservationService.isRoomAvailable(145)).thenReturn(true);
        when(reservationService.getPaymentStatus(1L)).thenReturn(ResponseEntity.ok("Paid"));

        ResponseEntity<Map<String, Object>> response = reservationController.getReservationWithDetails(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().containsKey("reservation"));
        assertTrue(response.getBody().containsKey("guestDetails"));
        assertTrue(response.getBody().containsKey("roomAvailable"));
        assertTrue(response.getBody().containsKey("paymentStatus"));
    }

    @Test
    public void testGetReservationWithDetails_NotFound() {
        when(reservationService.getReservationById(1L)).thenReturn(Optional.empty());

        ResponseEntity<Map<String, Object>> response = reservationController.getReservationWithDetails(1L);

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void testIsRoomReserved_True() {
        Reservation reservation = new Reservation();
        reservation.setRoomId(145);
        when(reservationService.getAllReservations()).thenReturn(List.of(reservation));

        ResponseEntity<Boolean> response = reservationController.isRoomReserved(101);

        assertFalse(response.getBody());
    }

    @Test
    public void testIsRoomReserved_False() {
        Reservation reservation = new Reservation();
        reservation.setRoomId(145);
        when(reservationService.getAllReservations()).thenReturn(List.of(reservation));

        ResponseEntity<Boolean> response = reservationController.isRoomReserved(101);

        assertFalse(response.getBody());
    }
}
