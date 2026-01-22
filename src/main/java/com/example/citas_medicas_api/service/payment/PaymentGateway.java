package com.example.citas_medicas_api.service.payment;

import java.math.BigDecimal;

public interface PaymentGateway {

    PaymentResult pay(BigDecimal amount);
}
