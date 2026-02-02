package com.hotel.reservation.feign;


import com.hotel.reservation.dto.PaymentRequestDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "payment-gateway")
public interface PaymentServiceClient {
    @GetMapping("/api/payments/status/{bookingId}")
    ResponseEntity<String> getPaymentStatusByBookingId(@PathVariable("bookingId") String bookingId);

    @PostMapping("/api/payments/create-link")
    ResponseEntity<String> createPaymentLink(@RequestBody PaymentRequestDTO request);

    @PutMapping("/api/payments/refund/{bookingId}")
    ResponseEntity<String> refundPayment(@PathVariable("bookingId") String bookingId);
}