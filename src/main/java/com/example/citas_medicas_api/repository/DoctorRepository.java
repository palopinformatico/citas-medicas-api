package com.example.citas_medicas_api.repository;

import com.example.citas_medicas_api.model.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {

}
