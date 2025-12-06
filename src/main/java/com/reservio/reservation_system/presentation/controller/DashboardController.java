package com.reservio.reservation_system.presentation.controller;

import com.reservio.reservation_system.domain.service.ReportService;
import com.reservio.reservation_system.presentation.dto.report.DashboardStatsDto;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("hd/dashboard")
public class DashboardController {
    private final ReportService reportService;

    @GetMapping("/today")
    public ResponseEntity<DashboardStatsDto> getTodayDashboard() {
        DashboardStatsDto stats = reportService.getTodayDashboardStats();
        return ResponseEntity.ok(stats);
    }
}
