package com.hotel.payment_service.controller;



import com.hotel.payment_service.dto.PaymentRequestDTO;
import com.hotel.payment_service.dto.PaymentRequestForStatus;
import com.hotel.payment_service.dto.PaymentResponseDTO;
import com.hotel.payment_service.model.Payment;
import com.hotel.payment_service.repository.PaymentRepository;
import com.hotel.payment_service.service.PaymentService;
import com.razorpay.RazorpayException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/payments")

public class PaymentController {

    private final PaymentService paymentService;
    private final PaymentRepository paymentRepository;

    public PaymentController(PaymentService paymentService, PaymentRepository paymentRepository) {
        this.paymentService = paymentService;
        this.paymentRepository = paymentRepository;
    }



    @PostMapping("/create-link")
    public ResponseEntity<String> createPaymentLink(@RequestBody PaymentRequestDTO request) {
        try {
            String paymentLink = paymentService.createPaymentLink(request);
            return ResponseEntity.ok(paymentLink);
        } catch (RazorpayException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to generate payment link: " + e.getMessage());
        }
    }


    @PostMapping("/create")
    public ResponseEntity<PaymentResponseDTO> createPayment(@Valid @RequestBody PaymentRequestDTO request) {
        try {
            PaymentResponseDTO response = paymentService.createPaymentOrder(request);
            return ResponseEntity.ok(response);
        } catch (RazorpayException e) {
            return ResponseEntity.internalServerError().body(new PaymentResponseDTO("Error processing payment"));
        }
    }

    @GetMapping("/success")
    public ResponseEntity<String> paymentSuccess() {
        return ResponseEntity.ok("Payment was successful! Thank you.");
    }

    @GetMapping("/cancel")
    public ResponseEntity<String> paymentCancel() {
        return ResponseEntity.ok("Payment was cancelled.");
    }

    @GetMapping("/rate-test")
    public ResponseEntity<String> testRateConnection(@RequestParam String roomType) {
        try {
            Double rate = paymentService.getRateForRoomType(roomType);
            return ResponseEntity.ok("Rate for room type '" + roomType + "': " + rate);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error connecting to rate-service: " + e.getMessage());
        }
    }

    @GetMapping("/status/{bookingId}")
    public ResponseEntity<PaymentRequestForStatus> getPaymentStatusByBookingId(@PathVariable String bookingId) {
        return ResponseEntity.ok(paymentService.getPaymentStatusByBookingId(bookingId));
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> handleRazorpayWebhook(HttpServletRequest request) throws IOException {
        String payload = request.getReader().lines().collect(Collectors.joining());

        // Extract event data
        JSONObject event = new JSONObject(payload);
        String eventType = event.getString("event");

        if ("payment.captured".equals(eventType)) {
            JSONObject payloadData = event.getJSONObject("payload").getJSONObject("payment").getJSONObject("entity");
            String paymentId = payloadData.getString("id");

            paymentRepository.findByPaymentIntentId(paymentId)
                    .ifPresent(payment -> {
                        payment.setStatus("SUCCEEDED");
                        paymentRepository.save(payment);
                    });
        }

        return ResponseEntity.ok("Received");
    }

    @PutMapping("/refund/{bookingId}")
    public ResponseEntity<String> refundPayment(@PathVariable String bookingId) {
        Payment payment = paymentRepository.findByBookingId(bookingId).orElse(null);
        if (payment == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Payment not found");
        }

        payment.setStatus("REFUNDED");
        paymentRepository.save(payment);

        return ResponseEntity.ok("Refund processed");
    }

    @GetMapping("/all")
    public ResponseEntity<List<PaymentRequestForStatus>> getAllPayments() {
        return ResponseEntity.ok(paymentService.findAll());
    }

    @GetMapping("/revenue")
    public ResponseEntity<Double> getTotalRevenue(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {

        List<Payment> payments = paymentRepository.findAll();

        Double total = payments.stream()
                .filter(p -> "CREATED".equalsIgnoreCase(p.getStatus()))
                .filter(p -> {
                    LocalDateTime created = p.getCreatedAt();
                    boolean match = true;
                    if (startDate != null) {
                        LocalDateTime start = LocalDateTime.parse(startDate);
                        match &= !created.isBefore(start);
                    }
                    if (endDate != null) {
                        LocalDateTime end = LocalDateTime.parse(endDate);
                        match &= !created.isAfter(end);
                    }
                    return match;
                })
                .mapToDouble(Payment::getAmount)
                .sum();

        System.out.println("Start: " + startDate + ", End: " + endDate);
        System.out.println("Total payments: " + total);

        return ResponseEntity.ok(total);
    }
}
