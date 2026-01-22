package com.example.citas_medicas_api.service;

import com.example.citas_medicas_api.exception.BusinessException;
import com.example.citas_medicas_api.model.entity.Appointment;
import com.example.citas_medicas_api.model.entity.Patient;
import com.example.citas_medicas_api.model.enums.AppointmentStatus;
import com.example.citas_medicas_api.repository.AppointmentRepository;
import com.example.citas_medicas_api.repository.PaymentRepository;
import com.example.citas_medicas_api.service.payment.PaymentGateway;
import com.example.citas_medicas_api.service.payment.PaymentResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private PaymentGateway paymentGateway;

    @InjectMocks
    private PaymentService paymentService;

    @Test
    void shouldPayAppointmentSuccessfully() {

        Patient patient = new Patient();
        patient.setId(1L);
        //patient.setRole(UserRole.PACIENTE);

        Appointment appointment = new Appointment();
        appointment.setId(1L);
        appointment.setPatient(patient);
        appointment.setStatus(AppointmentStatus.PENDIENTE);

        when(appointmentRepository.findById(1L))
                .thenReturn(Optional.of(appointment));

        when(paymentRepository.existsByAppointmentId(1L))
                .thenReturn(false);

        when(paymentGateway.pay(any(BigDecimal.class)))
                .thenReturn(new PaymentResult(true, "REF123"));

        paymentService.payAppointment(1L);

        verify(paymentRepository).save(any());
        verify(appointmentRepository).save(any());
    }

    @Test
    void shouldFailIfAlreadyPaid() {

        Patient patient = new Patient();
        patient.setId(1L);
        //patient.setRole(UserRole.PACIENTE);

        Appointment appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setStatus(AppointmentStatus.PAGADA);

        when(appointmentRepository.findById(1L))
                .thenReturn(Optional.of(appointment));

        assertThrows(
                BusinessException.class,
                () -> paymentService.payAppointment(1L)
        );
    }
}
