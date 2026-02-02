package com.hotel.payment_service.model;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;


import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "payments")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull(message = "Booking ID cannot be null")
        private String bookingId;

        @NotBlank(message = "Customer email is required")
        @Email(message = "Invalid email format")
        private String customerEmail;

        @NotNull(message = "Amount is required")
        @Positive(message = "Amount must be positive")
        private Double amount;

        @NotBlank(message = "Currency is required")
        @Pattern(regexp = "^[A-Z]{3}$", message = "Currency must be a 3-letter ISO code")
        private String currency;

        @NotBlank(message = "Payment Intent ID is required")
        private String paymentIntentId;

        @NotBlank(message = "Status is required")
        private String status;

        @NotNull(message = "Creation date is required")
        private LocalDateTime createdAt;

        // Getters and setters (or use Lombok @Getter/@Setter)
    }


