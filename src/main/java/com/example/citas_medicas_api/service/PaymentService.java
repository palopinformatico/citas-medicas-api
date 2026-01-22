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

        // 1️⃣ Buscar cita
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new BusinessException("Cita no encontrada"));

        // 2️⃣ Validar rol
        /*if (user.getRole() != UserRole.PACIENTE) {
            throw new BusinessException("Solo el paciente puede pagar la cita");
        }*/

        // 3️⃣ Validar dueño de la cita
        /*if (!appointment.getPatient().getId().equals(user.getId())) {
            throw new BusinessException("No puede pagar una cita que no es suya");
        }*/

        // 4️⃣ Validar estado
        if (appointment.getStatus() != AppointmentStatus.PENDIENTE) {
            throw new BusinessException("La cita no está pendiente de pago");
        }

        // 5️⃣ Validar pago previo
        if (paymentRepository.existsByAppointmentId(appointmentId)) {
            throw new BusinessException("La cita ya fue pagada");
        }

        // 6️⃣ Simular pago sandbox
        BigDecimal amount = BigDecimal.valueOf(20000);
        PaymentResult result = paymentGateway.pay(amount);

        if (!result.success()) {
            throw new BusinessException("Pago rechazado");
        }

        // 7️⃣ Guardar pago
        Payment payment = new Payment();
        payment.setAppointment(appointment);
        payment.setAmount(amount);
        payment.setPayment_reference(result.reference());
        payment.setCreatedAt(LocalDateTime.now());

        paymentRepository.save(payment);

        // 8️⃣ Actualizar estado de la cita
        appointment.setStatus(AppointmentStatus.PAGADA);
        appointmentRepository.save(appointment);
    }
}
