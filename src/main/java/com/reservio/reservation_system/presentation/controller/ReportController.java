package com.reservio.reservation_system.presentation.controller;

import com.reservio.reservation_system.domain.service.ReportService;
import com.reservio.reservation_system.presentation.dto.report.PaymentReportDto;
import com.reservio.reservation_system.presentation.dto.report.RoomOccupancyReportDto;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("hd/reports")
@AllArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/payments")
    public PaymentReportDto getPaymentReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end
    ) {
        return reportService.getPaymentReport(start, end);
    }

    // RAPORT OBŁOŻENIA
    @GetMapping("/occupancy")
    public List<RoomOccupancyReportDto> getRoomOccupancy(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end
    ) {
        return reportService.calculateRoomOccupancy(start, end);
    }

}
