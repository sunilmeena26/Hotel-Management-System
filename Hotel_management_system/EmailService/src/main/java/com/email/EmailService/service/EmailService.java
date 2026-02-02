package com.email.EmailService.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendOtpEmail(String toEmail, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Your StayEase OTP Code");

        message.setText(
                "Dear Guest,\n\n" +
                        "To proceed securely, please use the following One-Time Password (OTP): " + otp + "\n\n" +
                        "This code is valid for 10 minutes. If you did not request this, please ignore this message.\n\n" +
                        "Warm regards,\n" +
                        "The StayEase Team\n" +
                        "Where comfort meets elegance."
        );

        mailSender.send(message);
    }



    public void sendConfirmationEmail(String to, String bookingCode) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Your StayEase Booking Confirmation");

        message.setText(
                "Dear Guest,\n\n" +
                        "We’re delighted to confirm your booking with StayEase.\n\n" +
                        "Booking Code: " + bookingCode + "\n\n" +
                        "Thank you for choosing StayEase, where comfort meets elegance. We look forward to welcoming you and ensuring your stay is truly exceptional.\n\n" +
                        "If you have any questions or special requests, feel free to reach out to our concierge team.\n\n" +
                        "Warm regards,\n" +
                        "The StayEase Team\n" +
                        "Redefining hospitality since 1990."
        );

        mailSender.send(message);
    }

    public void sendCancellationEmail(String to, String bookingCode) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Your StayEase Booking Cancellation");

        message.setText(
                "Dear Guest,\n\n" +
                        "We regret to inform you that your booking with StayEase has been cancelled.\n\n" +
                        "Booking Code: " + bookingCode + "\n\n" +
                        "If this was unintentional or you’d like to rebook, our team is here to assist you. Any applicable refund will be processed shortly.\n\n" +
                        "We hope to welcome you again in the future and provide the exceptional experience you deserve.\n\n" +
                        "Warm regards,\n" +
                        "The StayEase Team\n" +
                        "Committed to comfort, even in cancellation."
        );

        mailSender.send(message);
    }

}
