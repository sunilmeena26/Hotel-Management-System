package com.hotel.reservation.controller;

import com.hotel.reservation.dto.GuestDTO;
import com.hotel.reservation.dto.ReservationDTO;
import com.hotel.reservation.model.Reservation;
import com.hotel.reservation.service.EmailService;
import com.hotel.reservation.service.ReservationService;
import jakarta.validation.Valid;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private EmailService emailService;


    // Add new reservation
    @PostMapping("/add")
    public ResponseEntity<ReservationDTO> createReservation(@Valid @RequestBody Reservation reservation) {
       // String username = authentication.getName(); // ✅ Extract from JWT
       // reservation.setUsername(username);
        return ResponseEntity.ok(reservationService.addReservation(reservation));
    }

    // Get all reservations
    @GetMapping("/getAll")
    public List<Reservation> getAllReservations() {
        return reservationService.getAllReservations();
    }

    // Get reservation by ID along with guest details, room availability, and payment status
    @GetMapping("/get/{id}")
    public ResponseEntity<Reservation> getReservationWithDetails(@PathVariable Long id) {
        return ResponseEntity.ok(reservationService.getReservationById(id));
    }
    @GetMapping("/room/{roomNumber}")
    public ResponseEntity<Boolean> isRoomReserved(@PathVariable int roomNumber) {
        // Check if any reservation exists for the given room
        List<Reservation> reservations = reservationService.getAllReservations();
        boolean reserved = reservations.stream()
                .anyMatch(r -> r.getRoomId() == roomNumber);
        return ResponseEntity.ok(reserved);
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<ReservationDTO> getReservationByCode(@PathVariable String code) {
        return ResponseEntity.ok(reservationService.getReservationByCode(code));
    }

    @PostMapping("/regenerate-payment-link/{code}")
    public ResponseEntity<String> regeneratePaymentLink(@PathVariable String code) {
        return ResponseEntity.ok("Link");
    }

    @GetMapping("/update-status/{code}")
    public ResponseEntity<String> updateStatus(@PathVariable String code) {
        String status = reservationService.updateReservationStatusAfterPayment(code);
        return ResponseEntity.ok(status);
    }

    @GetMapping("/user/{email}")
    public ResponseEntity<List<Reservation>> getReservationsByUserEmail(@PathVariable String email) {
        return ResponseEntity.ok(reservationService.getReservationsByUserEmail(email));
    }

    @PutMapping("/cancel/{id}")
    public ResponseEntity<String> cancelReservation(@PathVariable Long id) {
        boolean success = reservationService.cancelReservation(id);
        if (success) {
            return ResponseEntity.ok("Reservation cancelled and refund processed.");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Cancellation failed.");
        }
    }

    @GetMapping("/test-email")
    public String testEmail() {
        emailService.sendConfirmationEmail("sunilmeena641085@gmail.com", "TEST1234");
        return "Email sent!";
    }
}
