package com.reservio.reservation_system.domain.service;

import com.reservio.reservation_system.domain.repository.PaymentDao;
import com.reservio.reservation_system.domain.repository.ReservationDao;
import com.reservio.reservation_system.domain.repository.RoomDao;
import com.reservio.reservation_system.infrastructure.entity.PaymentEntity;
import com.reservio.reservation_system.infrastructure.entity.ReservationEntity;
import com.reservio.reservation_system.infrastructure.entity.RoomEntity;
import com.reservio.reservation_system.infrastructure.report.ReportCalculator;
import com.reservio.reservation_system.presentation.dto.report.DashboardStatsDto;
import com.reservio.reservation_system.presentation.dto.report.PaymentReportDto;
import com.reservio.reservation_system.presentation.dto.report.RoomOccupancyReportDto;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ReportService {

    private final ReservationDao reservationDao;
    private final RoomDao roomDao;
    private final ReportCalculator calculator;
    private final PaymentDao paymentDao;

    @Transactional
    public List<RoomOccupancyReportDto> calculateRoomOccupancy(LocalDateTime start, LocalDateTime end) {

        List<RoomEntity> rooms = roomDao.findAll();
        List<ReservationEntity> reservations =
                reservationDao.findAllByCheckInOutDates(start, end);

        List<RoomOccupancyReportDto> report = new ArrayList<>();

        long totalDays = ChronoUnit.DAYS.between(start.toLocalDate(), end.toLocalDate());

        for (RoomEntity room : rooms) {

            List<ReservationEntity> roomReservations = reservations.stream()
                    .filter(r -> r.getRm().getId().equals(room.getId()))
                    .toList();

            int occupiedDays = roomReservations.stream()
                    .mapToInt(r -> calculator.calculateOccupiedDays(r, start, end))
                    .sum();

            float occupancyRate = calculator.calculateOccupancyRate(occupiedDays, totalDays);

            report.add(new RoomOccupancyReportDto(
                    room.getRmNumber(),
                    occupiedDays,
                    (int) totalDays,
                    occupancyRate,
                    start.toLocalDate(),
                    end.toLocalDate()
            ));
        }

        return report;
    }

    public PaymentReportDto getPaymentReport(LocalDate startDate, LocalDate endDate) {

        List<ReservationEntity> reservations =
                reservationDao.findReservaitonsWithApprovedPaymentsInPeriod(startDate, endDate);

        List<PaymentEntity> paidPayments = reservations.stream()
                .flatMap(r -> r.getPayments().stream())
                .filter(p -> p.getPmts().getPmtsName().equalsIgnoreCase("PAID"))
                .toList();

        BigDecimal totalRevenue = calculator.calculateTotalRevenue(paidPayments);
        long count = calculator.calculatePaymentCount(paidPayments);
        BigDecimal average = calculator.calculateAveragePayment(paidPayments);
        String method = calculator.findMostUsedPaymentMethod(paidPayments);

        return new PaymentReportDto(
                totalRevenue,
                count,
                average,
                method,
                startDate,
                endDate
        );
    }

    public DashboardStatsDto getTodayDashboardStats() {
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.plusDays(1).atStartOfDay();

        BigDecimal todayRevenue = paymentDao.findPaidRevenueForToday(startOfDay, endOfDay);

        long todayReservationCount = reservationDao.countCreatedToday(startOfDay, endOfDay);

        List<ReservationEntity> activeToday = reservationDao.findAllByCheckInOutDates(startOfDay, endOfDay);

        List<RoomEntity> allRooms = roomDao.findAll();
        int occupiedRoomsToday = (int) activeToday.stream()
                .map(ReservationEntity::getRm)
                .distinct()
                .count();

        int availableRoomsToday = allRooms.size() - occupiedRoomsToday;

        float hotelOccupancyRate = allRooms.isEmpty() ? 0f :
                (occupiedRoomsToday * 100.0f) / allRooms.size();

        return new DashboardStatsDto(
                todayRevenue,
                (int) todayReservationCount,
                hotelOccupancyRate,
                availableRoomsToday
        );
    }
}
