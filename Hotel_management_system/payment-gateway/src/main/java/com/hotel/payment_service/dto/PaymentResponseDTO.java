package com.hotel.payment_service.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponseDTO {

    private Long paymentId;
    private String paymentIntentId;
    private String clientSecret;
    private String status;


    private String message;

 public PaymentResponseDTO(String message) {
 this.message = message;
}

}
