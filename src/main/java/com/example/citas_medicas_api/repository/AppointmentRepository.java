package com.example.citas_medicas_api.repository;

import com.example.citas_medicas_api.model.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    boolean existsByDoctorIdAndDateAndTime(
        Long doctorId,
        LocalDate date,
        LocalTime time
    );

    List<Appointment> findByDoctorId(Long doctorId);

    List<Appointment> findByPatientIdOrderByDateAscTimeAsc(Long patientId);
}
