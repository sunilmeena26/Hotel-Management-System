package com.hotel.reservation.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "email-service")
public interface EmailServiceClient {

    @PostMapping("/email/send-cancellation/{email}/{bookingCode}")
    public void sendCancellation(
            @PathVariable("email") String email,
            @PathVariable("bookingCode") String bookingCode);

    @PostMapping("/email/send-confirmation/{email}/{bookingCode}")
    public void sendConfirmation(
            @PathVariable("email") String email,
            @PathVariable("bookingCode") String bookingCode);
}
