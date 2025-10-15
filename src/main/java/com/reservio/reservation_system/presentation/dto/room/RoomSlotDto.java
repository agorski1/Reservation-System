package com.reservio.reservation_system.presentation.dto.room;

import com.reservio.reservation_system.presentation.dto.user.UserDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
@AllArgsConstructor
public class RoomSlotDto {
    private LocalDateTime start;
    private LocalDateTime end;
    private UserDto user;
}
