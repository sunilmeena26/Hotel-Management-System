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
@Getter
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

    public int getRoomNumber() {
        return roomNumber;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public double getPrice() {
        return price;
    }

    public String getRoomType() {
        return roomType;
    }

    public String getRoomPath() {
        return roomPath;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public void setRoomPath(String roomPath) {
        this.roomPath = roomPath;
    }
}
