package com.reservio.reservation_system.presentation.dto.reservation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReservationDto {
    private long id;
    String firstName;
    String lastName;
    String email;
    private String phoneNumber;
    String roomType;
    Short guestCount;
    String status;
    LocalDateTime checkInDate;
    LocalDateTime checkOutDate;

}
