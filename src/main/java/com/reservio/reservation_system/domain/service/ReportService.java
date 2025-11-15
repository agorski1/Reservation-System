package com.reservio.reservation_system.domain.service;

import com.reservio.reservation_system.domain.repository.ReservationDao;
import com.reservio.reservation_system.domain.repository.RoomDao;
import com.reservio.reservation_system.infrastructure.entity.PaymentEntity;
import com.reservio.reservation_system.infrastructure.entity.ReservationEntity;
import com.reservio.reservation_system.infrastructure.entity.RoomEntity;
import com.reservio.reservation_system.presentation.dto.report.PaymentReportDto;
import com.reservio.reservation_system.presentation.dto.report.RoomOccupancyReportDto;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ReportService {
    private final ReservationDao reservationDao;
    private final RoomDao roomDao;

    @Transactional()
    public List<RoomOccupancyReportDto> calculateRoomOccupancy(LocalDateTime startDate, LocalDateTime endDate) {

        List<RoomEntity> rooms = roomDao.findAll();

        List<ReservationEntity> reservations =
                reservationDao.findAllByCheckInOutDates(startDate, endDate);

        List<RoomOccupancyReportDto> report = new ArrayList<>();

        for (RoomEntity room : rooms) {

            List<ReservationEntity> roomReservations = reservations.stream()
                    .filter(r -> r.getRm().getId().equals(room.getId()))
                    .toList();

            int occupiedDays = 0;

            for (ReservationEntity res : roomReservations) {

                LocalDateTime resStart = res.getRsvCheckInDate();
                LocalDateTime resEnd = res.getRsvCheckOutDate();

                LocalDateTime actualStart = resStart.isBefore(startDate) ? startDate : resStart;
                LocalDateTime actualEnd = resEnd.isAfter(endDate) ? endDate : resEnd;

                long days = ChronoUnit.DAYS.between(actualStart.toLocalDate(), actualEnd.toLocalDate());
                if (days > 0) {
                    occupiedDays += days;
                }
            }

            int totalDays = (int) ChronoUnit.DAYS.between(
                    startDate.toLocalDate(),
                    endDate.toLocalDate()
            );

            float occupancyRate = totalDays > 0
                    ? (occupiedDays * 100f) / totalDays
                    : 0f;

            RoomOccupancyReportDto dto = new RoomOccupancyReportDto();
            dto.setRoomNumber(room.getRmNumber());
            dto.setOccupiedDays(occupiedDays);
            dto.setTotalDays(totalDays);
            dto.setOccupancyRate(occupancyRate);
            dto.setStartDate(startDate.toLocalDate());
            dto.setEndDate(endDate.toLocalDate());

            report.add(dto);
        }

        return report;
    }

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