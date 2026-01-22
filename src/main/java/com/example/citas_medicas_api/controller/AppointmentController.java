package com.example.citas_medicas_api.controller;

import com.example.citas_medicas_api.model.dto.CreateAppointmentRequest;
import com.example.citas_medicas_api.model.entity.Appointment;
import com.example.citas_medicas_api.service.AppointmentService;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    private final AppointmentService service;

    public AppointmentController(AppointmentService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Appointment createAppointment(
            @RequestBody CreateAppointmentRequest request
    ) {        
        return service.createAppointment(request);
    }

    @PutMapping("/{id}/confirm")
    @ResponseStatus(HttpStatus.OK)
    public Appointment confirmAppointment(
            @PathVariable Long id
    ) {
        return service.confirmAppointment(id);
    }

    @GetMapping("/doctor/{doctorId}")
    public List<Appointment> getAppointments(
            @PathVariable Long doctorId
    ) {
        return service.getAppointments(doctorId);
    }

    @GetMapping("/patient/{patientId}")
    public List<Appointment> getAgendaByPatient(
            @PathVariable Long patientId
    ) {
        return service.getAgendaByPatient(patientId);
    }
}
