package com.reservio.reservation_system.presentation.dto.report;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
@Setter
public class RoomOccupancyReportDto {
    private Integer roomNumber;
    private Integer occupiedDays;
    private Integer totalDays;
    private Float occupancyRate; // procent zajetosci
    private LocalDate startDate;
    private LocalDate endDate;
}
