package com.example.citas_medicas_api.service;

import com.example.citas_medicas_api.model.dto.CreateAppointmentRequest;

import java.time.LocalDate;
import java.time.LocalTime;

public class TestData {

    public static CreateAppointmentRequest createAppointmentRequest() {
        CreateAppointmentRequest req = new CreateAppointmentRequest();
        req.setUserRole("PATIENT");
        req.setPatientId(1L);
        req.setDoctorId(2L);
        req.setDate(LocalDate.now().plusDays(1));
        req.setTime(LocalTime.of(10, 0));
        return req;
    }
}
