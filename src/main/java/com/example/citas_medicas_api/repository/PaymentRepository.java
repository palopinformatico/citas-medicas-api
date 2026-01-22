package com.example.citas_medicas_api.repository;

import com.example.citas_medicas_api.model.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    boolean existsByAppointmentId(Long appointmentId);
}
