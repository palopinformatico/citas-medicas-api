package com.example.citas_medicas_api.service;

import com.example.citas_medicas_api.service.payment.PaymentGateway;
import com.example.citas_medicas_api.service.payment.PaymentResult;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class SandboxPaymentGateway implements PaymentGateway {

    @Override
    public PaymentResult pay(BigDecimal amount) {
        return new PaymentResult(true, UUID.randomUUID().toString());
    }
}
