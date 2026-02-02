package com.hotel.room.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Entity
@Table(name = "rooms")
@Data
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Room {

    @Id
    private int roomNumber;

    @NotNull
    private boolean isAvailable;

    @Positive(message = "Price must be greater than 0")
    private double price;

    @NotBlank(message = "Room type is required")
    @Column(nullable = false)
    private String roomType;

    @NotNull
    private String roomPath;
}
