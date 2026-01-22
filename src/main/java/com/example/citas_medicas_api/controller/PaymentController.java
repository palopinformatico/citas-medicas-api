package com.example.citas_medicas_api.controller;

import com.example.citas_medicas_api.service.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService service;

    public PaymentController(PaymentService service) {
        this.service = service;
    }

    @PostMapping("/{appointmentId}")
    @ResponseStatus(HttpStatus.OK)
    public void payAppointment(
            @PathVariable Long appointmentId
    ) {
        service.payAppointment(appointmentId);
    }
}
