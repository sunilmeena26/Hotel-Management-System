package com.email.EmailService.controller;

import com.email.EmailService.config.RabbitMQConfig;
import com.email.EmailService.dto.EmailRequest;
import com.email.EmailService.service.EmailService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/email")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @Autowired
    private RabbitTemplate rabbitTemplate;


    @PostMapping("/send-otp")
    public ResponseEntity<String> sendOtp(@RequestBody EmailRequest request) {
        emailService.sendOtpEmail(request.getEmail(), request.getOtp());
        return ResponseEntity.ok("OTP sent successfully");
    }

    @PostMapping("/send-confirmation/{email}/{bookingCode}")
    public void sendConfirmation(
            @PathVariable String email,
            @PathVariable String bookingCode) {

        emailService.sendConfirmationEmail(email, bookingCode);
    }

    @PostMapping("/send-cancellation/{email}/{bookingCode}")
    public void sendCancellation(
            @PathVariable String email,
            @PathVariable String bookingCode) {

        emailService.sendCancellationEmail(email, bookingCode);
    }


    @PostMapping("/send")
    public ResponseEntity<String> sendEmail(@RequestBody EmailRequest request) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.EMAIL_QUEUE, request);
        return ResponseEntity.ok("Email request queued successfully");
    }



}

