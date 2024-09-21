package com.example.careflow_backend.controller;

import com.example.careflow_backend.config.RSAKeyRecord;
import com.example.careflow_backend.config.jwtConfig.CustomUserDetails;
import com.example.careflow_backend.config.jwtConfig.JwtTokenUtils;
import com.example.careflow_backend.dto.AppointmentDto;
import com.example.careflow_backend.dto.MedicalHistoryDto;
import com.example.careflow_backend.dto.TokenType;
import com.example.careflow_backend.service.AppointmentService;
import com.example.careflow_backend.service.MedicalHistoryService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api")
@CrossOrigin("http://localhost:3000")
public class MedicalHistoryController {

    private final MedicalHistoryService medicalHistoryService;
    private final JwtTokenUtils jwtTokenUtils;

    // @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/add_medical_history")
    public ResponseEntity<String> addMedicalHistory(@Valid @RequestBody MedicalHistoryDto medicalHistoryDto) {
        try {

            // Access the SecurityContext to get the current authenticated user
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            System.out.println("Auth: " + authentication);

            if (authentication != null && authentication.getPrincipal() instanceof Jwt) {
                Jwt jwt = (Jwt) authentication.getPrincipal();
                // Extract user ID from JWT token
                Long userId = jwtTokenUtils.getUserId(jwt);

                if (userId != null) {
                    System.out.println("UserID: " + userId);
                    // Pass the userId to your service method
                    // Call the service to add the appointment
                    medicalHistoryService.addMedicalHistory(medicalHistoryDto);
                    return ResponseEntity.ok("Appointment created successfully");
                } else {
                    System.out.println("UserID is null");
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.emptyList().toString());
                }
            } else {
                System.out.println("Principal is not an instance of Jwt");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.emptyList().toString());
            }
        } catch (Exception e) {
            // Log the exception and return an error response
            log.error("Error while creating appointment", e);
            return ResponseEntity.status(500).body("Failed to create appointment: " + e.getMessage());
        }
    }

    @GetMapping("/get_medical_history_patient/{patientId}")
    public ResponseEntity<List<MedicalHistoryDto>> getMedicalHistoryByPatientId(@PathVariable Long patientId) {
        try {
            // Access the SecurityContext to get the current authenticated user
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            System.out.println("Auth: " + authentication);

            if (authentication != null && authentication.getPrincipal() instanceof Jwt) {
                Jwt jwt = (Jwt) authentication.getPrincipal();
                // Extract user ID from JWT token
                Long userId = jwtTokenUtils.getUserId(jwt);

                if (userId != null) {
                    System.out.println("UserID: " + userId);
                    // Pass the userId to your service method
                    List<MedicalHistoryDto> medicalHistoryDtos = medicalHistoryService.getMedicalHistoryByPatientId(patientId);
                    return ResponseEntity.ok(medicalHistoryDtos);
                } else {
                    System.out.println("UserID is null");
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.emptyList());
                }
            } else {
                System.out.println("Principal is not an instance of Jwt");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.emptyList());
            }
        } catch (ResponseStatusException e) {
            // Log the exception and return an error response
            log.error("Error while fetching appointments", e);
            return ResponseEntity.status(e.getStatusCode()).body(Collections.emptyList());
        } catch (Exception e) {
            // Return an empty list and internal server error
            log.error("[MedicalHistoryController:GetAllAppointmentsbyPatientId] Unexpected error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
        }
    }

    @GetMapping("/get_medical_history_doctor/{doctorId}")
    public ResponseEntity<List<MedicalHistoryDto>> getMedicalHistoryByDoctorId(@PathVariable Long doctorId) {
        try {
            // Access the SecurityContext to get the current authenticated user
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            System.out.println("Auth: " + authentication);

            if (authentication != null && authentication.getPrincipal() instanceof Jwt) {
                Jwt jwt = (Jwt) authentication.getPrincipal();
                // Extract user ID from JWT token
                Long userId = jwtTokenUtils.getUserId(jwt);

                if (userId != null) {
                    System.out.println("UserID: " + userId);
                    // Pass the userId to your service method
                    List<MedicalHistoryDto> medicalHistoryDtos = medicalHistoryService.getMedicalHistoryByDoctorId(doctorId);
                    return ResponseEntity.ok(medicalHistoryDtos);
                } else {
                    System.out.println("UserID is null");
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.emptyList());
                }
            } else {
                System.out.println("Principal is not an instance of Jwt");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.emptyList());
            }
        } catch (ResponseStatusException e) {
            // Log the exception and return an error response
            log.error("Error while fetching appointments", e);
            return ResponseEntity.status(e.getStatusCode()).body(Collections.emptyList());
        } catch (Exception e) {
            // Return an empty list and internal server error
            log.error("[MedicalHistoryController:GetAllAppointmentsbyDoctorId] Unexpected error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
        }
    }
}
