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
import java.util.Map;

@Service
@AllArgsConstructor
public class ReportService {

    private final ReservationDao reservationDao;
    private final RoomDao roomDao;
    private final ReportCalculator calculator;
    private final PaymentDao paymentDao;

    @Transactional
    public List<RoomOccupancyReportDto> calculateRoomOccupancy(LocalDateTime start, LocalDateTime end) {

        LocalDate startDate = start.toLocalDate();
        LocalDate endDate = end.toLocalDate();

        int totalDays = (int) ChronoUnit.DAYS.between(startDate, endDate) + 1;

        List<RoomEntity> rooms = roomDao.findAll();
        List<ReservationEntity> reservations =
                reservationDao.findAllByCheckInOutDates(start, end);

        List<RoomOccupancyReportDto> report = new ArrayList<>();

        for (RoomEntity room : rooms) {

            // Rezerwacje dla pokoju
            List<ReservationEntity> roomReservations = reservations.stream()
                    .filter(r -> r.getRm().getId().equals(room.getId()))
                    .toList();

            int reservationCount = roomReservations.size();

            // Liczenie zajętych dni
            int occupiedDays = roomReservations.stream()
                    .mapToInt(r -> calculator.calculateOccupiedDays(r, startDate, endDate))
                    .sum();

            float occupancyRate = totalDays == 0
                    ? 0f
                    : (float) occupiedDays / totalDays;

            // Tworzenie DTO
            RoomOccupancyReportDto dto = new RoomOccupancyReportDto();
            dto.setStartDate(startDate);
            dto.setEndDate(endDate);
            dto.setTotalDays(totalDays);

            dto.setRoomNumber(room.getRmNumber());
            dto.setRoomType(room.getRt().getId().shortValue());
            dto.setCapacity(room.getRt().getRtCapacity());

            dto.setReservationCount(reservationCount);
            dto.setOccupiedDays(occupiedDays);
            dto.setOccupancyRate(occupancyRate);

            report.add(dto);
        }

        return report;
    }


    public PaymentReportDto getPaymentReport(LocalDateTime start, LocalDateTime end) {

        // Pobierz wszystkie płatności z datą w zakresie raportu i statusem PAID
        List<PaymentEntity> paidPayments = paymentDao.findAllPaidPaymentsInPeriod(start, end);

        int totalDays = (int) ChronoUnit.DAYS.between(start.toLocalDate(), end.toLocalDate()) + 1;

        BigDecimal totalRevenue = calculator.calculateTotalRevenue(paidPayments);
        long paymentCount = calculator.calculatePaymentCount(paidPayments);
        BigDecimal avg = calculator.calculateAveragePayment(paidPayments);
        BigDecimal maxPayment = calculator.findMaxPayment(paidPayments);
        String mostUsed = calculator.findMostUsedPaymentMethod(paidPayments);

        Map<String, BigDecimal> revenuePerMethod = calculator.revenuePerPaymentMethod(paidPayments);
        Map<String, Long> countPerMethod = calculator.countPerPaymentMethod(paidPayments);
        Map<LocalDate, BigDecimal> revenuePerDay = calculator.revenuePerDay(paidPayments);

        PaymentReportDto dto = new PaymentReportDto();
        dto.setStartDate(start.toLocalDate());
        dto.setEndDate(end.toLocalDate());
        dto.setTotalDays(totalDays);
        dto.setTotalRevenue(totalRevenue);
        dto.setPaymentCount(paymentCount);
        dto.setAveragePaymentAmount(avg);
        dto.setMaxPaymentAmount(maxPayment);
        dto.setMostUsedPaymentMethod(mostUsed);
        dto.setRevenuePerPaymentMethod(revenuePerMethod);
        dto.setPaymentCountPerPaymentMethod(countPerMethod);
        dto.setRevenuePerDay(revenuePerDay);

        return dto;
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
