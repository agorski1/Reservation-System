package com.reservio.reservation_system.domain.service;

import com.reservio.reservation_system.domain.repository.*;
import com.reservio.reservation_system.infrastructure.entity.*;
import com.reservio.reservation_system.presentation.dto.payment.PaymentResponseDto;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class PaymentService {
    private final PaymentDao paymentDao;
    private final ReservationDao reservationDao;
    private final ReservationStatusDao reservationStatusDao;
    private final PaymentStatusDao paymentStatusDao;
    private final PaymentMethodDao paymentMethodDao;

    @Transactional
    public PaymentResponseDto processPayment(Long reservationId, Long userId, Float amount, String paymentMethod) {

        if (amount == null || amount <= 0) {
            throw new IllegalArgumentException("Invalid payment amount");
        }

        ReservationEntity reservation = reservationDao.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found"));

        if (!reservation.getUsr().getId().equals(userId)) {
            throw new IllegalArgumentException("Reservation does not belong to user");
        }

        PaymentEntity payment = new PaymentEntity();
        payment.setRsv(reservation);
        payment.setPmtAmount(BigDecimal.valueOf(amount));
        payment.setPmtDate(LocalDateTime.now());

        PaymentStatusEntity statusPaid = paymentStatusDao.findByPmtsName("PAID")
                .orElseThrow(() -> new IllegalArgumentException("Payment status PAID not found"));
        payment.setPmts(statusPaid);

        PaymentMethodEntity methodEntity = paymentMethodDao.findByPmtmName(paymentMethod)
                .orElseThrow(() -> new IllegalArgumentException("Payment method not found: " + paymentMethod));
        payment.setPmtm(methodEntity);

        paymentDao.save(payment);

        BigDecimal totalPaid = paymentDao.getPaidAmountReservation(reservationId);
        BigDecimal roomPrice = reservation.getRm().getRt().getRtPricePerNight();

        String statusName;
        if (totalPaid.compareTo(BigDecimal.ZERO) == 0) {
            statusName = "PENDING";
        } else if (totalPaid.compareTo(roomPrice) < 0) {
            statusName = "PARTIAL-PAID";
        } else {
            statusName = "PAID";
        }

        ReservationStatusEntity newStatus = reservationStatusDao.findByRsvsName(statusName)
                .orElseThrow(() -> new IllegalArgumentException("Reservation status not found: " + statusName));
        reservation.setRsvs(newStatus);
        reservationDao.save(reservation);

        return new PaymentResponseDto(
                reservation.getId(),
                totalPaid.floatValue(),
                paymentMethod
        );
    }
}
