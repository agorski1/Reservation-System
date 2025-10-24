package com.reservio.reservation_system.presentation.dto.room;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class AvailableRoomTypeDto {
    private long id;
    private String name;
    private int capacity;
    private BigDecimal pricePerNight;
    private BigDecimal totalPrice;
    private List<String> amenities;
}

