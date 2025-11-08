package com.reservio.reservation_system.presentation.dto.reservation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class RoomReservationRequestDto {
    private Long deskId;
    private Short guestCount;
    private LocalDateTime from;
    private LocalDateTime to;
}
