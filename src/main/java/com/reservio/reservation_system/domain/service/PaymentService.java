package com.reservio.reservation_system.domain.service;

import com.reservio.reservation_system.domain.repository.PaymentDao;
import com.reservio.reservation_system.domain.repository.PaymentStatusDao;
import com.reservio.reservation_system.domain.repository.ReservationDao;
import com.reservio.reservation_system.domain.repository.ReservationStatusDao;
import com.reservio.reservation_system.infrastructure.entity.PaymentEntity;
import com.reservio.reservation_system.infrastructure.entity.PaymentStatusEntity;
import com.reservio.reservation_system.infrastructure.entity.ReservationEntity;
import com.reservio.reservation_system.infrastructure.entity.ReservationStatusEntity;
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

    @Transactional
    public PaymentResponseDto processPayment(Long reservationId, Long userId, Float amount, String paymentMethod) {

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

        paymentDao.save(payment);

        BigDecimal totalPaid = paymentDao.getPaidAmountReservation(reservationId);

        BigDecimal roomPrice = reservation.getRm().getRt().getRtPricePerNight();

        String statusName;
        if (totalPaid.compareTo(BigDecimal.ZERO) == 0) {
            statusName = "PENDING_PAYMENT";
        } else if (totalPaid.compareTo(roomPrice) < 0) {
            statusName = "PARTIALLY_PAID";
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