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
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer totalDays; // liczba dni raportu

    private Short roomNumber;
    private Short roomType;
    private Long capacity;

    private Integer reservationCount; // liczba pobytow
    private Integer occupiedDays; // laczna liczba dni, kiedy pokoj byl zajety w danym okresie
    private Float occupancyRate; // procent zajetosci
}
