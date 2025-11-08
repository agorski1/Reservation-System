package com.reservio.reservation_system.presentation.dto.payment;

public record PaymentRequestDto(Long reservationId, Float amount, String paymentMethod) {}
