package com.example.citas_medicas_api.service.payment;

public record PaymentResult(
        boolean success,
        String reference
) {}
