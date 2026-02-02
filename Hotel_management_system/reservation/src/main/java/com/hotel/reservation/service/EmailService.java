package com.hotel.reservation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendConfirmationEmail(String to, String bookingCode) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Booking Confirmed");
        message.setText("Your booking with code " + bookingCode + " has been confirmed. Thank you for choosing StayEase!");
        mailSender.send(message);
    }

    public void sendCancellationEmail(String to, String bookingCode) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Booking Cancelled");
        message.setText("Your booking with code " + bookingCode + " has been cancelled. Refund will be processed shortly.");
        mailSender.send(message);
    }
}