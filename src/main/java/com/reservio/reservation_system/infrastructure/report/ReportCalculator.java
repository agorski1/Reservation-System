package com.reservio.reservation_system.infrastructure.report;

import com.reservio.reservation_system.infrastructure.entity.PaymentEntity;
import com.reservio.reservation_system.infrastructure.entity.ReservationEntity;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
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

    public static int calculateOccupiedDays(ReservationEntity reservation, LocalDate start, LocalDate end) {

        LocalDate resStart = reservation.getRsvCheckInDate().toLocalDate();
        LocalDate resEnd = reservation.getRsvCheckOutDate().toLocalDate();

        if (resStart.isBefore(start)) {
            resStart = start;
        }

        LocalDate reportEnd = end.plusDays(1);

        if (resEnd.isAfter(reportEnd)) {
            resEnd = reportEnd;
        }

        if (!resStart.isBefore(resEnd)) {
            return 0;
        }

        return (int) ChronoUnit.DAYS.between(resStart, resEnd);
    }

    public BigDecimal findMaxPayment(List<PaymentEntity> payments) {
        return payments.stream()
                .map(PaymentEntity::getPmtAmount)
                .max(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);
    }

    public Map<String, BigDecimal> revenuePerPaymentMethod(List<PaymentEntity> payments) {
        return payments.stream()
                .collect(Collectors.groupingBy(
                        p -> p.getPmtm().getPmtmName(),
                        Collectors.reducing(BigDecimal.ZERO, PaymentEntity::getPmtAmount, BigDecimal::add)
                ));
    }

    public Map<String, Long> countPerPaymentMethod(List<PaymentEntity> payments) {
        return payments.stream()
                .collect(Collectors.groupingBy(
                        p -> p.getPmtm().getPmtmName(),
                        Collectors.counting()
                ));
    }

    public Map<LocalDate, BigDecimal> revenuePerDay(List<PaymentEntity> payments) {
        return payments.stream()
                .collect(Collectors.groupingBy(
                        p -> p.getPmtDate().toLocalDate(),
                        Collectors.reducing(BigDecimal.ZERO, PaymentEntity::getPmtAmount, BigDecimal::add)
                ));
    }
}
