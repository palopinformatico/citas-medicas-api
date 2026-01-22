package com.example.citas_medicas_api.service;

import com.example.citas_medicas_api.exception.BusinessException;
import com.example.citas_medicas_api.model.dto.CreateAppointmentRequest;
import com.example.citas_medicas_api.model.entity.Appointment;
import com.example.citas_medicas_api.model.entity.Patient;
import com.example.citas_medicas_api.model.entity.Doctor;
import com.example.citas_medicas_api.model.enums.AppointmentStatus;
import com.example.citas_medicas_api.repository.AppointmentRepository;
import com.example.citas_medicas_api.repository.PatientRepository;
import com.example.citas_medicas_api.repository.DoctorRepository;
import org.springframework.stereotype.Service;

//import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;

    public AppointmentService(AppointmentRepository appointmentRepository,
                              PatientRepository patientRepository,
                              DoctorRepository doctorRepository) {
        this.appointmentRepository = appointmentRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
    }

    public Appointment createAppointment(
            CreateAppointmentRequest request
    ) {
        if (request.getUserRole().compareTo(com.example.citas_medicas_api.model.enums.Users.PATIENT.toString()) != 0) {
            throw new BusinessException("Solo los pacientes pueden crear citas");
        }

        // 2️⃣ Validar horario
        validateSchedule(request.getTime());

        // 3️⃣ Validar paciente
        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new BusinessException("Paciente no encontrado"));

        // 3️⃣ Validar médico
        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new BusinessException("Médico no encontrado"));

        // 4️⃣ Validar disponibilidad
        boolean exists = appointmentRepository.existsByDoctorIdAndDateAndTime(
                doctor.getId(),
                request.getDate(),
                request.getTime()
        );

        if (exists) {
            throw new BusinessException("Horario no disponible");
        }

        // 5️⃣ Crear cita
        Appointment appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setDate(request.getDate());
        appointment.setTime(request.getTime());
        appointment.setStatus(AppointmentStatus.PENDIENTE);

        return appointmentRepository.save(appointment);
    }

    private void validateSchedule(LocalTime time) {

        boolean morning =
                !time.isBefore(LocalTime.of(7, 0)) &&
                !time.isAfter(LocalTime.of(12, 0));

        boolean afternoon =
                !time.isBefore(LocalTime.of(14, 0)) &&
                !time.isAfter(LocalTime.of(18, 0));

        if (!(morning || afternoon)) {
            throw new BusinessException("Horario fuera de atención");
        }
    }

    public Appointment confirmAppointment(Long appointmentId) {

        // 1️⃣ Buscar cita
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new BusinessException("Cita no encontrada"));

        // 4️⃣ Validar estado
        if (appointment.getStatus() == AppointmentStatus.CONFIRMADA) {
            throw new BusinessException("La cita ya está confirmada");
        }

        if (appointment.getStatus() != AppointmentStatus.PAGADA) {
            throw new BusinessException("La cita debe estar PAGADA para confirmarla");
        }

        // 5️⃣ Confirmar
        appointment.setStatus(AppointmentStatus.CONFIRMADA);

        return appointmentRepository.save(appointment);
    }

    public List<Appointment> getAppointments(Long doctorId) {

        // 3️⃣ Validar médico
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new BusinessException("Médico no encontrado"));

        // 3️⃣ Buscar citas
        return appointmentRepository.findByDoctorId(
                doctor.getId()
        );
    }

    public List<Appointment> getAgendaByPatient(Long patientId) {

        // 1️⃣ Control de acceso
        /*if (user.getRole() == UserRole.PACIENTE &&
            !user.getId().equals(patientId)) {
            throw new BusinessException("No puede ver la agenda de otro paciente");
        }*/

        // 2️⃣ (Opcional) Validar que el paciente exista
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new BusinessException("Paciente no encontrado"));

        // 3️⃣ Buscar agenda
        return appointmentRepository.findByPatientIdOrderByDateAscTimeAsc(patient.getId());
    }
}
