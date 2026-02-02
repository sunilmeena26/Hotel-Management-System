package com.email.EmailService.consumer;

import com.email.EmailService.dto.EmailRequest;
import com.email.EmailService.service.EmailService;
import com.email.EmailService.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmailConsumer {

    @Autowired
    private EmailService emailService;

    @RabbitListener(queues = RabbitMQConfig.EMAIL_QUEUE)
    public void consumeEmailQueue(EmailRequest request) {
        switch (request.getType()) {
            case "otp":
                emailService.sendOtpEmail(request.getEmail(), request.getOtp());
                break;
            case "confirmation":
                emailService.sendConfirmationEmail(request.getEmail(), request.getBookingCode());
                break;
            case "cancellation":
                emailService.sendCancellationEmail(request.getEmail(), request.getBookingCode());
                break;
            default:
                System.out.println("Unknown email type: " + request.getType());
        }
    }
}