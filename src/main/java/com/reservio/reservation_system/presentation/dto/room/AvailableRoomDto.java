package com.reservio.reservation_system.presentation.dto.room;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AvailableRoomDto {
    private Long id;
    private Long number;
}
