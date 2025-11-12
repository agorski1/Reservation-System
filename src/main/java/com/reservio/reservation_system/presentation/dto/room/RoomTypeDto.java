package com.reservio.reservation_system.presentation.dto.room;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoomTypeDto {
    private long id;
    private String name;
    private int capacity;
    private BigDecimal pricePerNight;
    private String description;
}
