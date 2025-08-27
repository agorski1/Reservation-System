package com.reservio.reservation_system.domain.service;

import com.reservio.reservation_system.domain.repository.ReservationDao;
import com.reservio.reservation_system.domain.repository.RoomDao;
import com.reservio.reservation_system.infrastructure.entity.PaymentEntity;
import com.reservio.reservation_system.infrastructure.entity.ReservationEntity;
import com.reservio.reservation_system.presentation.dto.report.PaymentReportDto;
import com.reservio.reservation_system.presentation.dto.report.RoomOccupancyReportDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ReportService {
    private final RoomDao roomDao;
    private final ReservationDao reservationDao;

//    public List<RoomOccupancyReportDto> calculateRoomOccupancy(LocalDate startDate, LocalDate endDate) {
//
//    }

    public PaymentReportDto getPaymentReport(LocalDate startDate, LocalDate endDate) {
        List<ReservationEntity> reservations = reservationDao
                .findReservaitonsWithApprovedPaymentsInPeriod(startDate, endDate);

        List<PaymentEntity> paidPayments = reservations.stream()
                .flatMap(rsv -> rsv.getPayments().stream())
                .filter(p -> p.getPmts().getPmtsName().equalsIgnoreCase("PAID"))
                .toList();

        BigDecimal totalRevenue = paidPayments.stream()
                .map(PaymentEntity::getPmtAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        long paymentCount = paidPayments.size();

        BigDecimal averagePaymentAmount = paymentCount > 0
                ? totalRevenue.divide(BigDecimal.valueOf(paymentCount), 2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;

        String mostUsedPaymentMethod = paidPayments.stream()
                .collect(Collectors.groupingBy(p -> p.getPmtm().getPmtmName(), Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("N/A");

        return new PaymentReportDto(
                "ALL_ROOMS",
                totalRevenue,
                paymentCount,
                averagePaymentAmount,
                mostUsedPaymentMethod,
                startDate,
                endDate
        );
    }
}
