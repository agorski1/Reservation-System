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
    private BigDecimal totalRevenue;
    private long paymentCount;
    private BigDecimal averagePaymentAmount;
    private String mostUsedPaymentMethod;
    private long totalReservationDays;

    private LocalDate startDate;
    private LocalDate endDate;
}