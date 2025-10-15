package com.reservio.reservation_system.presentation.controller;

import com.reservio.reservation_system.domain.service.ReportService;
import com.reservio.reservation_system.presentation.dto.report.PaymentReportDto;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("rs/reports")
@AllArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/payments")
    public ResponseEntity<PaymentReportDto> getPaymentReport(
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate from,
            @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate to) {

        return ResponseEntity.ok(reportService.getPaymentReport(from, to));




//    @GetMapping("/occupancy")
//    public ResponseEntity<Response> getOccupancyReport(
//            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate from,
//            @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {

//
    }



}
