package com.reservio.reservation_system.presentation.dto.report;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DashboardStatsDto {
    private BigDecimal todayRevenue;
    private int todayReservationCount;
    private float hotelOccupancyRate;
    private int availableRoomsToday;
}
