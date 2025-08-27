package com.reservio.reservation_system.presentation.dto.report;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class PaymentReportDto {
    private String roomNumber;
    private BigDecimal totalRevenue; // suma wszystkich platnosci
    private long paymentCount; // liczba dokonanych platnosci
    private BigDecimal averagePaymentAmount; // totalRevenue/paymentCount
    private String mostUsedPaymentMethod;

    private LocalDate startDate;
    private LocalDate endDate;
}