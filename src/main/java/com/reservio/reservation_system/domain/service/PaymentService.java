package com.reservio.reservation_system.domain.service;

import com.reservio.reservation_system.domain.repository.*;
import com.reservio.reservation_system.infrastructure.entity.*;
import com.reservio.reservation_system.presentation.dto.payment.PaymentResponseDto;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Set;

@Service
@AllArgsConstructor
public class PaymentService {
    private final PaymentDao paymentDao;
    private final ReservationDao reservationDao;
    private final ReservationStatusDao reservationStatusDao;
    private final PaymentStatusDao paymentStatusDao;
    private final PaymentMethodDao paymentMethodDao;
    private final UserDao userDao;


    @Transactional
    public PaymentResponseDto processPayment(Long reservationId, String email, Float amount, String paymentMethod) {

        if (amount == null || amount <= 0) {
            throw new IllegalArgumentException("Invalid payment amount");
        }

        UserEntity user = userDao.findByUsrEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        ReservationEntity reservation = reservationDao.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found"));

        if (!reservation.getUsr().getId().equals(user.getId())) {
            throw new IllegalArgumentException("Reservation does not belong to user");
        }

        Set<String> allowedStatuses = Set.of("Pending", "Confirmed", "Partial-Paid");
        if (!allowedStatuses.contains(reservation.getRsvs().getRsvsName())) {
            throw new IllegalStateException("Cannot process payment for reservation with status: "
                    + reservation.getRsvs().getRsvsName());
        }

        long days = ChronoUnit.DAYS.between(
                reservation.getRsvCheckInDate().toLocalDate(),
                reservation.getRsvCheckOutDate().toLocalDate()
        );
        BigDecimal totalPrice = reservation.getRm().getRt().getRtPricePerNight()
                .multiply(BigDecimal.valueOf(days));

        BigDecimal totalPaidSoFar = paymentDao.getPaidAmountReservation(reservationId);
        if (totalPaidSoFar == null) {
            totalPaidSoFar = BigDecimal.ZERO;
        }

        BigDecimal remainingAmount = totalPrice.subtract(totalPaidSoFar);
        if (BigDecimal.valueOf(amount).compareTo(remainingAmount) > 0) {
            throw new IllegalArgumentException("Payment exceeds remaining amount: " + remainingAmount);
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
        String statusName = calcStatus(totalPaid, totalPrice);

        ReservationStatusEntity newStatus = reservationStatusDao.findByRsvsName(statusName)
                .orElseThrow(() -> new IllegalArgumentException("Reservation status not found: " + statusName));
        reservation.setRsvs(newStatus);
        reservationDao.save(reservation);

        remainingAmount = totalPrice.subtract(totalPaid);
        if (remainingAmount.compareTo(BigDecimal.ZERO) < 0) {
            remainingAmount = BigDecimal.ZERO;
        }

        return new PaymentResponseDto(
                reservation.getId(),
                totalPaid.floatValue(),
                remainingAmount.floatValue()
        );
    }

    private String calcStatus(BigDecimal totalPaid, BigDecimal totalPrice) {
        if (totalPaid.compareTo(BigDecimal.ZERO) == 0) return "Pending";
        if (totalPaid.compareTo(totalPrice) < 0) return "Partial-Paid";
        return "Paid";
    }
}
