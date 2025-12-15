package com.reservio.reservation_system.presentation.dto.report;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentReportDto {
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer totalDays; // liczba dni raportu

    private BigDecimal totalRevenue; // suma wszystkich platnosci
    private long paymentCount; // liczba dokonanych platnosci
    private BigDecimal averagePaymentAmount; // srednia kwota platnosci = totalRevenue/paymentCount
    private BigDecimal maxPaymentAmount;

    private String mostUsedPaymentMethod;

    private Map<String, BigDecimal> revenuePerPaymentMethod; // suma przychodow na kazda metode platnosci
    private Map<String, Long> paymentCountPerPaymentMethod; // liczba platnosci w podziale na metody platnosci

    private Map<LocalDate, BigDecimal> revenuePerDay; // przychod dzienny
}