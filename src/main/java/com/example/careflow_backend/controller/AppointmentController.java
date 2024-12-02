package com.example.careflow_backend.controller;

import com.example.careflow_backend.config.RSAKeyRecord;
import com.example.careflow_backend.config.jwtConfig.CustomUserDetails;
import com.example.careflow_backend.config.jwtConfig.JwtTokenUtils;
import com.example.careflow_backend.dto.AppointmentDetailsDto;
import com.example.careflow_backend.dto.AppointmentDto;
import com.example.careflow_backend.dto.TokenType;
import com.example.careflow_backend.service.AppointmentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.InvalidDataAccessApiUsageException;
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
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final JwtTokenUtils jwtTokenUtils;

//    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/add_appointment")
    public ResponseEntity<String> addAppointment(@Valid @RequestBody AppointmentDto appointmentDto) {
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
                    appointmentService.addAppointment(appointmentDto ,userId);
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

    @GetMapping("/get_allAppointment")
    public ResponseEntity<List<AppointmentDto>> GetAllAppointments() {
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
                    List<AppointmentDto> appointmentDto = appointmentService.getAllAppointments(userId);
                    return ResponseEntity.ok(appointmentDto);
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
            log.error("[UserController:GetAllAppointments] Unexpected error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
        }
    }

    @GetMapping("/Appointments")
    public ResponseEntity<List<AppointmentDto>> GetAllAppointmentsByAdmin() {
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
                    List<AppointmentDto> appointmentDto = appointmentService.getAllAppointmentsByAdmin();
                    return ResponseEntity.ok(appointmentDto);
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
            log.error("[UserController:GetAllAppointments] Unexpected error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
        }
    }

    @PostMapping("/add_appointment_with_payment")
    public ResponseEntity<String> addAppointmentWithPay( @RequestBody AppointmentDto appointmentDto) {
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
                    /// Call the service to add the appointment
                    System.out.println("Iddddddddd " + appointmentDto.getDoctorId());
                    System.out.println("Date " + appointmentDto.getAppointmentDate());
                    appointmentService.addAppointmentWithPay(appointmentDto, userId);
                    log.info("Appointment creation successful" );
                    return ResponseEntity.ok("Appointment payment and created successfully");
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

    @GetMapping("/getAppointmentDetails/{id}/details")
    public ResponseEntity<AppointmentDetailsDto> getAppointmentDetails(@PathVariable Long id) {
        AppointmentDetailsDto details = appointmentService.getAppointmentDetails(id);
        return ResponseEntity.ok(details);
    }
}
