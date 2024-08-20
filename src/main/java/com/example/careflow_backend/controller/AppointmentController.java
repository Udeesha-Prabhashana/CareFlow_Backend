package com.example.careflow_backend.controller;

import com.example.careflow_backend.dto.AppointmentDto;
import com.example.careflow_backend.service.AppointmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api")
@CrossOrigin("http://localhost:3000")
public class AppointmentController {

    private final AppointmentService appointmentService;

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/add_appointment")
    public ResponseEntity<String> addAppointment(@Valid @RequestBody AppointmentDto appointmentDto) {
        try {
            // Call the service to add the appointment
            appointmentService.addAppointment(appointmentDto);
            return ResponseEntity.ok("Appointment created successfully");
        } catch (Exception e) {
            // Log the exception and return an error response
            log.error("Error while creating appointment", e);
            return ResponseEntity.status(500).body("Failed to create appointment: " + e.getMessage());
        }
    }
}
