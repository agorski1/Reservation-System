package com.reservio.reservation_system.presentation.dto.room;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class RoomDto {
    private Long id;
    private Long number;
}
