package com.reservio.reservation_system.presentation.dto.reservation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class UserReservationDto {
    private Long reservationId;
    private Long roomNumber;
    private LocalDateTime from;
    private LocalDateTime to;
}
