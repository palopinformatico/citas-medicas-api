package com.example.citas_medicas_api.repository;

import com.example.citas_medicas_api.model.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, Long> {

}
