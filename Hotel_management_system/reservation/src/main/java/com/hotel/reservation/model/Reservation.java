package com.hotel.reservation.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "reservations")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;

    @NotNull(message = "User email is required")
    private String userEmail;

    @Min(value = 0,message = "Number of children must be between 0-2")
    @Max(value = 2,message = "Number of children must be between 0-2")
    private int numChildren;

    @Min(value = 0,message = "Number of adults must be between 0-2")
    @Max(value = 2,message = "Number of adults must be between 0-2")
    private int numAdults;

    @NotNull
    private LocalDate checkInDate;

    @NotNull
    private LocalDate checkOutDate;

    @NotNull
    private String status;

    @Min(value = 1,message = "Night Must be positive")
    private int numNights;

    @NotNull(message = "Guest ID is required")
    private Long guestId;

    @NotNull(message = "Room ID is required")
    private int roomId;

    @Column(length = 500)
    private String paymentLink;

}
