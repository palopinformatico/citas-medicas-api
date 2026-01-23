package com.example.citas_medicas_api.service;

import com.example.citas_medicas_api.exception.BusinessException;
import com.example.citas_medicas_api.model.entity.Appointment;
import com.example.citas_medicas_api.model.entity.Payment;
import com.example.citas_medicas_api.model.enums.AppointmentStatus;
import com.example.citas_medicas_api.repository.AppointmentRepository;
import com.example.citas_medicas_api.repository.PaymentRepository;
import com.example.citas_medicas_api.service.payment.PaymentGateway;
import com.example.citas_medicas_api.service.payment.PaymentResult;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class PaymentService {

    private final AppointmentRepository appointmentRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentGateway paymentGateway;

    public PaymentService(AppointmentRepository appointmentRepository,
                          PaymentRepository paymentRepository,
                          PaymentGateway paymentGateway) {
        this.appointmentRepository = appointmentRepository;
        this.paymentRepository = paymentRepository;
        this.paymentGateway = paymentGateway;
    }

    public void payAppointment(Long appointmentId) {

        // Buscar cita
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new BusinessException("Cita no encontrada"));

        // Validar estado
        if (appointment.getStatus() != AppointmentStatus.PENDIENTE) {
            throw new BusinessException("La cita no está pendiente de pago");
        }

        // Validar pago previo
        if (paymentRepository.existsByAppointmentId(appointmentId)) {
            throw new BusinessException("La cita ya fue pagada");
        }

        // Simular pago sandbox
        BigDecimal amount = BigDecimal.valueOf(20000);
        PaymentResult result = paymentGateway.pay(amount);

        if (!result.success()) {
            throw new BusinessException("Pago rechazado");
        }

        // Guardar pago
        Payment payment = new Payment();
        payment.setAppointment(appointment);
        payment.setAmount(amount);
        payment.setPayment_reference(result.reference());
        payment.setCreatedAt(LocalDateTime.now());

        paymentRepository.save(payment);

        // Actualizar estado de la cita
        appointment.setStatus(AppointmentStatus.PAGADA);
        appointmentRepository.save(appointment);
    }
}
