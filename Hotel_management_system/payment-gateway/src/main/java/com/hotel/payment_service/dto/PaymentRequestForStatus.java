package com.hotel.payment_service.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PaymentRequestForStatus {
        private String bookingId;
        private String customerEmail;
        private Double amount;
        private String currency;
        private String status;
        private LocalDateTime createdAt;
    }

