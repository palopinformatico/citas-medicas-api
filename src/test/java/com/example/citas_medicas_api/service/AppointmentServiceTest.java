package com.example.citas_medicas_api.service;

import com.example.citas_medicas_api.exception.BusinessException;
import com.example.citas_medicas_api.model.entity.Appointment;
import com.example.citas_medicas_api.model.entity.Patient;
import com.example.citas_medicas_api.model.entity.Doctor;
import com.example.citas_medicas_api.model.enums.AppointmentStatus;
import com.example.citas_medicas_api.repository.AppointmentRepository;
import com.example.citas_medicas_api.repository.DoctorRepository;
import com.example.citas_medicas_api.repository.PatientRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private DoctorRepository doctorRepository;

    @InjectMocks
    private AppointmentService appointmentService;

    @Test
    void shouldCreateAppointmentSuccessfully() {

        Patient patient = new Patient();
        patient.setId(1L);
        //patient.setRole(UserRole.PACIENTE);

        Doctor doctor = new Doctor();
        doctor.setId(2L);
        //doctor.setRole(UserRole.MEDICO);

        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(doctorRepository.findById(2L)).thenReturn(Optional.of(doctor));
        when(appointmentRepository.existsByDoctorIdAndDateAndTime(any(), any(), any()))
                .thenReturn(false);
        when(appointmentRepository.save(any()))
                .thenAnswer(i -> i.getArgument(0));

        Appointment appointment = appointmentService.createAppointment(
                TestData.createAppointmentRequest()
        );

        assertNotNull(appointment);
        assertEquals(AppointmentStatus.PENDIENTE, appointment.getStatus());
    }

    @Test
    void shouldFailIfConfirmWithoutPayment() {

        Appointment appointment = new Appointment();
        appointment.setStatus(AppointmentStatus.PENDIENTE);

        Doctor doctor = new Doctor();
        doctor.setId(1L);
        //doctor.setRole(UserRole.MEDICO);

        appointment.setDoctor(doctor);

        when(appointmentRepository.findById(1L))
                .thenReturn(Optional.of(appointment));

        assertThrows(
                BusinessException.class,
                () -> appointmentService.confirmAppointment(1L)
        );
    }
}
