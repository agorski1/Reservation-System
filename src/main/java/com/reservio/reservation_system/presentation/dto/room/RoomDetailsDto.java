package com.reservio.reservation_system.presentation.dto.room;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class RoomDetailsDto {
    private Long roomId;
    private Short roomNumber;
    private String activityStatus;
    private List<RoomSlotDto> occupiedSlots;
}
