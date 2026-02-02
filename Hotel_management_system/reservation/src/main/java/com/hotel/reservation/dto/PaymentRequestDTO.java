package com.hotel.reservation.dto;
import lombok.Data;

@Data
public class PaymentRequestDTO {
    private String bookingId;
    private String customerEmail;
    private Double amount;
    private String currency;
}
