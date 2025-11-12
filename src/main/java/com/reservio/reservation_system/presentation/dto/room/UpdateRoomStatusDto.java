package com.reservio.reservation_system.presentation.dto.room;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateRoomStatusDto {
    private Long id;
    private String status;
}