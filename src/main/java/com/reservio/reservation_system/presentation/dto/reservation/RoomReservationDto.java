package com.reservio.reservation_system.presentation.dto.reservation;

import com.reservio.reservation_system.presentation.dto.User.UserDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class RoomReservationDto {
    private Long reservationId;
    private LocalDate from;
    private LocalDate to;
    private UserDto user;
}
