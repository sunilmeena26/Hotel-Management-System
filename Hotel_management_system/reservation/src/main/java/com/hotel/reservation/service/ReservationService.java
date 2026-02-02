package com.hotel.reservation.service;

import com.hotel.reservation.dto.GuestDTO;
import com.hotel.reservation.dto.PaymentRequestDTO;
import com.hotel.reservation.dto.ReservationDTO;
import com.hotel.reservation.exception.ReservationException;
import com.hotel.reservation.feign.EmailServiceClient;
import com.hotel.reservation.feign.GuestServiceClient;
import com.hotel.reservation.feign.PaymentServiceClient;
import com.hotel.reservation.feign.RoomServiceClient;
import com.hotel.reservation.model.Reservation;
import com.hotel.reservation.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class ReservationService {



    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private RoomServiceClient roomServiceClient;

    @Autowired
    private GuestServiceClient guestServiceClient;

    @Autowired
    private PaymentServiceClient paymentServiceClient;

    @Autowired
    private EmailServiceClient emailServiceClient;

    public static String generateFourDigitId() {
        LocalDateTime now = LocalDateTime.now();
        String timeStamp = now.format(DateTimeFormatter.ofPattern("MMddHHmmss")); // MonthDayHourMinuteSecond

        // Use the last 6 digits to keep it within limit
        return timeStamp.substring(timeStamp.length() - 4);
    }


    public ReservationDTO addReservation(Reservation reservation) {
        Boolean isAvailable = roomServiceClient.isRoomAvailable(reservation.getRoomId());
        if (!isAvailable) {
            throw new ReservationException("Room is not available!");
        }

        GuestDTO guest = guestServiceClient.getGuestById(reservation.getGuestId());

        long numberOfNights = ChronoUnit.DAYS.between(reservation.getCheckInDate(), reservation.getCheckOutDate());
        double roomRate = roomServiceClient.getPriceByRoomId(reservation.getRoomId());
        double baseAmount = roomRate * numberOfNights;
        double taxAmount = baseAmount * 0.12;
        double totalAmount = baseAmount + taxAmount;

        if (totalAmount <= 0) {
            throw new ReservationException("Invalid payment amount");
        }

        String reservationId = "RE" + generateFourDigitId();
        reservation.setCode(reservationId);

        roomServiceClient.updateRoomById(reservation.getRoomId());


        PaymentRequestDTO paymentRequestDTO = new PaymentRequestDTO();
        paymentRequestDTO.setAmount(totalAmount);
        paymentRequestDTO.setCurrency("INR");
        paymentRequestDTO.setCustomerEmail(guest.getEmail());
        paymentRequestDTO.setBookingId(reservationId);

        System.out.println("Creating payment link with amount: " + paymentRequestDTO.getAmount());

        ResponseEntity<String> responseEntity = paymentServiceClient.createPaymentLink(paymentRequestDTO);

        System.out.println("Payment link created: " + responseEntity.getBody());
        System.out.println("Booking Id "+reservationId);

        reservation.setPaymentLink(responseEntity.getBody());

        reservationRepository.save(reservation);

        ReservationDTO dto = new ReservationDTO();
        dto.setGuestName(guest.getName());
        dto.setCheckInDate(reservation.getCheckInDate());
        dto.setCheckOutDate(reservation.getCheckOutDate());
        dto.setReservationNumber(reservationId);
        dto.setRoomNumber(reservation.getRoomId());
        dto.setGuestCode(reservation.getGuestId());
        dto.setMassageSuccessFully("Your Reservation Is Successfully Completed");
        dto.setPaymentMassage("Your Total Payment Is: " + totalAmount);
        dto.setPaymentLink(responseEntity.getBody());

        return dto;
    }

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    public Reservation getReservationById(Long id) {
        return reservationRepository.findById(id).orElseThrow(()->new ReservationException("Reservation not found"));
    }
    public GuestDTO getGuestDetails(Long guestId) {
        return guestServiceClient.getGuestById(guestId);
    }

    public Boolean isRoomAvailable(int roomId) {
        return roomServiceClient.isRoomAvailable(roomId);
    }

    public ResponseEntity<String> getPaymentStatus(String reservationId) {
        return paymentServiceClient.getPaymentStatusByBookingId(reservationId);
    }
    public boolean isRoomReserved(int roomNumber) {
        return reservationRepository.findAll()
                .stream()
                .anyMatch(r -> r.getRoomId() == roomNumber);
    }

    public ReservationDTO getReservationByCode(String code) {
        Reservation reservation =  reservationRepository.findByCode(code)
                .orElseThrow(() -> new ReservationException("Reservation not found"));

        GuestDTO guest = guestServiceClient.getGuestById(reservation.getGuestId());

        long numberOfNights = ChronoUnit.DAYS.between(
                reservation.getCheckInDate(), reservation.getCheckOutDate());

        double roomRate = roomServiceClient.getPriceByRoomId(reservation.getRoomId());
        double baseAmount = roomRate * numberOfNights;
        double taxAmount = baseAmount * 0.12;
        double totalAmount = baseAmount + taxAmount;

        ReservationDTO reservationDTO = new ReservationDTO();
        reservationDTO.setGuestName(guest.getName());
        reservationDTO.setCheckInDate(reservation.getCheckInDate());
        reservationDTO.setCheckOutDate(reservation.getCheckOutDate());
        reservationDTO.setReservationNumber(reservation.getCode());
        reservationDTO.setRoomNumber(reservation.getRoomId());
        reservationDTO.setGuestCode(reservation.getGuestId());
        reservationDTO.setMassageSuccessFully("Your Reservation Is SuccessFull Completed");
        reservationDTO.setPaymentMassage("Your Total Payment Is: " + totalAmount);
        reservationDTO.setPaymentLink(reservation.getPaymentLink()); // ✅ Use stored link



        return reservationDTO;
    }


    public String updateReservationStatusAfterPayment(String reservationCode) {
        Reservation reservation = reservationRepository.findByCode(reservationCode)
                .orElseThrow(() -> new ReservationException("Reservation not found"));

        ResponseEntity<String> paymentStatusResponse = paymentServiceClient.getPaymentStatusByBookingId(reservationCode);
        String paymentStatus = paymentStatusResponse.getBody();
        System.out.println("paymentStatus for update : " + paymentStatusResponse.getBody());


        if (paymentStatus.contains("CREATED")) {
            reservation.setStatus("CONFIRMED");
            reservationRepository.save(reservation);
            emailServiceClient.sendConfirmation(reservation.getUserEmail(), reservation.getCode());
        }

        return paymentStatus;
    }


    public List<Reservation> getReservationsByUserEmail(String email) {
        return reservationRepository.findByUserEmail(email);
    }


    public boolean cancelReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId).orElse(null);
        if (reservation == null || reservation.getStatus().equalsIgnoreCase("CANCELLED")) {
            return false;
        }

        reservation.setStatus("CANCELLED");
        reservationRepository.save(reservation);

        try {
            ResponseEntity<String> paymentStatusResponse = paymentServiceClient.getPaymentStatusByBookingId(reservation.getCode());
            String paymentStatus = paymentStatusResponse.getBody();
            System.out.println("PaymentStatus: " + paymentStatusResponse.getBody());

            if (paymentStatus.contains("CREATED")) {
                paymentServiceClient.refundPayment(reservation.getCode());
            }
        } catch (Exception e) {
            System.out.println("Payment service error: " + e.getMessage());
        }

        try {
            roomServiceClient.updateRoomToAvailableById(reservation.getRoomId());
        } catch (Exception e) {
            System.out.println("Room service error: " + e.getMessage());
        }

        // ✅ Send cancellation email
        emailServiceClient.sendCancellation(reservation.getUserEmail(), reservation.getCode());

        return true;
    }


}
