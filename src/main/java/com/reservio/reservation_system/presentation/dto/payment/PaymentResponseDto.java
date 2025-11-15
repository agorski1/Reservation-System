package com.reservio.reservation_system.presentation.dto.payment;

import java.math.BigDecimal;

public record PaymentResponseDto(Long reservationId, Float totalPaid, Float remainingAmount) {}
