package com.email.EmailService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailRequest {
    private String type; // otp, confirmation, cancellation
    private String email;
    private String otp;
    private String bookingCode;
}
