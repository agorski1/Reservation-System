package com.reservio.reservation_system.presentation.dto.reservation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class DeskReservationRequestDto {
    private Long deskId;
    private LocalDateTime from;
    private LocalDateTime to;
}
