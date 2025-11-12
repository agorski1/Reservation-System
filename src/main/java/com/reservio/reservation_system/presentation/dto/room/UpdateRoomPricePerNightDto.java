package com.reservio.reservation_system.presentation.dto.room;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateRoomPricePerNightDto {
    @NotBlank(message = "Status nie może być pusty")
    private float pricePerNight;
}
