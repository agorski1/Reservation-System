package com.reservio.reservation_system.presentation.dto.report;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RoomOccupancyReportDto {
    private Short roomNumber;
    private Integer occupiedDays;
    private Integer totalDays;
    private Float occupancyRate; // procent zajetosci

    private LocalDate startDate;
    private LocalDate endDate;
}
