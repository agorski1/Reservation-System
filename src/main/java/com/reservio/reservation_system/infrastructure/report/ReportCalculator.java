package com.reservio.reservation_system.infrastructure.report;

import com.reservio.reservation_system.infrastructure.entity.PaymentEntity;
import com.reservio.reservation_system.infrastructure.entity.ReservationEntity;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Component
@NoArgsConstructor
public class ReportCalculator {

    public BigDecimal calculateTotalRevenue(List<PaymentEntity> payments) {
        return payments.stream()
                .map(PaymentEntity::getPmtAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public long calculatePaymentCount(List<PaymentEntity> payments) {
        return payments.size();
    }

    public BigDecimal calculateAveragePayment(List<PaymentEntity> payments) {
        long count = payments.size();
        if (count == 0) return BigDecimal.ZERO;

        BigDecimal total = calculateTotalRevenue(payments);
        return total.divide(BigDecimal.valueOf(count), 2, RoundingMode.HALF_UP);
    }

    public String findMostUsedPaymentMethod(List<PaymentEntity> payments) {
        return payments.stream()
                .collect(Collectors.groupingBy(p -> p.getPmtm().getPmtmName(), Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("N/A");
    }

    public static int calculateOccupiedDays(ReservationEntity reservation, LocalDateTime start, LocalDateTime end) {
        LocalDateTime actualStart = reservation.getRsvCheckInDate().isBefore(start) ? start : reservation.getRsvCheckInDate();
        LocalDateTime actualEnd = reservation.getRsvCheckOutDate().isAfter(end) ? end : reservation.getRsvCheckOutDate();

        if (!actualEnd.isAfter(actualStart)) return 0;

        long seconds = ChronoUnit.SECONDS.between(actualStart, actualEnd);
        return (int) Math.ceil(seconds / 86400.0);
    }

    public float calculateOccupancyRate(long occupiedDays, long totalDays) {
        if (totalDays == 0) return 0f;
        return (occupiedDays * 100f) / totalDays;
    }
}
