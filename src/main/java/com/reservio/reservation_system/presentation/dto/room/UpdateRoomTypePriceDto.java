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
public class UpdateRoomTypePriceDto {
    private Long roomTypeId;
    private BigDecimal pricePerNight;
}
