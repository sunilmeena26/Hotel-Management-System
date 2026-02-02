package com.hotel.reservation.service;

import com.hotel.reservation.dto.ReservationDTO;
import com.hotel.reservation.feign.GuestServiceClient;
import com.hotel.reservation.feign.PaymentServiceClient;
import com.hotel.reservation.feign.RoomServiceClient;
import com.hotel.reservation.dto.GuestDTO;
import com.hotel.reservation.model.Reservation;
import com.hotel.reservation.repository.ReservationRepository;
import com.hotel.reservation.service.ReservationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private RoomServiceClient roomServiceClient;

    @Mock
    private GuestServiceClient guestServiceClient;

    @Mock
    private PaymentServiceClient paymentServiceClient;

    @InjectMocks
    private ReservationService reservationService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddReservation_Success() {
        Reservation reservation = new Reservation();
        reservation.setRoomId(145);
        reservation.setGuestId(2L);
        reservation.setCheckInDate(LocalDate.of(2025, 7, 10));
        reservation.setCheckOutDate(LocalDate.of(2025, 7, 13));
        reservation.setStatus("CONFIRMED"); // Optional but good for completeness

        GuestDTO guestDTO = new GuestDTO();
        guestDTO.setName("John Doe");

        when(roomServiceClient.isRoomAvailable(145)).thenReturn(true);
        when(guestServiceClient.getGuestById(2L)).thenReturn(guestDTO);
        when(reservationRepository.save(reservation)).thenReturn(reservation);
        when(roomServiceClient.getPriceByRoomId(145)).thenReturn(1000.00);

        ReservationDTO result = reservationService.addReservation(reservation);

        assertNotNull(result);
        assertEquals("John Doe", result.getGuestName());
        verify(roomServiceClient).isRoomAvailable(145);
        verify(guestServiceClient).getGuestById(2L);
        verify(reservationRepository).save(reservation);
    }

    @Test
    public void testAddReservation_RoomNotAvailable() {
        Reservation reservation = new Reservation();
        reservation.setRoomId(101);

        when(roomServiceClient.isRoomAvailable(101)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            reservationService.addReservation(reservation);
        });

        assertEquals("Room is not available!", exception.getMessage());
    }

    @Test
    public void testGetAllReservations() {
        List<Reservation> reservations = Arrays.asList(new Reservation(), new Reservation());
        when(reservationRepository.findAll()).thenReturn(reservations);

        List<Reservation> result = reservationService.getAllReservations();

        assertEquals(2, result.size());
    }

    @Test
    public void testGetReservationById() {
        Reservation reservation = new Reservation();
        reservation.setId(1L);
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));

        Optional<Reservation> result = reservationService.getReservationById(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
    }

    @Test
    public void testGetGuestDetails() {
        GuestDTO guestDTO = new GuestDTO();
        guestDTO.setName("Alice");

        when(guestServiceClient.getGuestById(101L)).thenReturn(guestDTO);

        GuestDTO result = reservationService.getGuestDetails(101L);

        assertEquals("Alice", result.getName());
    }

    @Test
    public void testIsRoomAvailable() {
        when(roomServiceClient.isRoomAvailable(101)).thenReturn(true);

        Boolean result = reservationService.isRoomAvailable(101);

        assertTrue(result);
    }

    @Test
    public void testGetPaymentStatus() {
        ResponseEntity<String> response = ResponseEntity.ok("Paid");

        when(paymentServiceClient.getPaymentStatusByBookingId(1L)).thenReturn(response);

        ResponseEntity<String> result = reservationService.getPaymentStatus(1L);

        assertEquals("Paid", result.getBody());
    }

    @Test
    public void testIsRoomReserved_True() {
        Reservation reservation = new Reservation();
        reservation.setRoomId(101);

        when(reservationRepository.findAll()).thenReturn(List.of(reservation));

        boolean result = reservationService.isRoomReserved(101);

        assertTrue(result);
    }

    @Test
    public void testIsRoomReserved_False() {
        Reservation reservation = new Reservation();
        reservation.setRoomId(102);

        when(reservationRepository.findAll()).thenReturn(List.of(reservation));

        boolean result = reservationService.isRoomReserved(101);

        assertFalse(result);
    }
}

