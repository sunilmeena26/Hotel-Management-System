package com.hotel.room.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomDTO {
    private int roomNumber;
    private boolean isAvailable;
    private double price;
    private String roomType;
    private String roomPath;
}
