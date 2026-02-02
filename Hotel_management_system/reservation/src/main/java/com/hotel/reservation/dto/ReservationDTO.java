package com.hotel.reservation.dto;

import lombok.*;

import java.time.LocalDate;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReservationDTO {

    private String guestName;
    private String massageSuccessFully;
    private String paymentMassage;
    private String reservationNumber;
    private int roomNumber;
    private Long guestCode;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private String paymentLink;


}
