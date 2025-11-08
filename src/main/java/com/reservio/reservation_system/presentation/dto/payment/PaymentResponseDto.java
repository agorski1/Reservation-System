package com.reservio.reservation_system.presentation.dto.payment;

public record PaymentResponseDto(Long reservationId, Float amount, String paymentMethod) {}
