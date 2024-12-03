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
import java.util.Map;

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

    @PostMapping("/{appointmentId}/cancel")
    public ResponseEntity<String> cancelAppointment(@PathVariable Long appointmentId) {
        try {
            appointmentService.cancelAppointment(appointmentId);
            return ResponseEntity.ok("Appointment canceled successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to cancel appointment: " + e.getMessage());
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

    @GetMapping("/get_allAppointment/doctor")
    public ResponseEntity<List<AppointmentDto>> GetAllAppointmentsForDoctors() {
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
                    List<AppointmentDto> appointmentDto = appointmentService.getAllAppointmentsDoctor(userId);
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

    @GetMapping("/get_appointments/{type}")
    public ResponseEntity<Map<String, Integer>> getAppointmentsCount(@PathVariable String type) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.getPrincipal() instanceof Jwt) {
                Jwt jwt = (Jwt) authentication.getPrincipal();
                Long doctorId = jwtTokenUtils.getUserId(jwt);

                log.info("Fetching appointment count for doctorId: {}", doctorId);
                int count = appointmentService.getAppointmentCount(doctorId, type);
                return ResponseEntity.ok(Collections.singletonMap("count", count));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.emptyMap());
            }
        } catch (Exception e) {
            log.error("Error while fetching appointment count", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyMap());
        }
    }


    private Long getDoctorId() {
        try {
            // Get the authentication object from the SecurityContext
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            // Check if the authentication object is valid and contains a JWT token
            if (authentication != null && authentication.getPrincipal() instanceof Jwt) {
                Jwt jwt = (Jwt) authentication.getPrincipal();

                // Use jwtTokenUtils to extract the user ID (assumed to be doctorId here)
                Long doctorId = jwtTokenUtils.getUserId(jwt);

                // Log the doctor ID for debugging purposes
                log.info("Authenticated doctor ID: {}", doctorId);

                // Return the doctor ID
                return doctorId;
            } else {
                // Log and throw an exception if authentication is invalid
                log.error("Authentication is invalid or missing JWT token.");
                throw new RuntimeException("Unauthorized: JWT token is invalid or missing.");
            }
        } catch (Exception e) {
            // Handle any unexpected exceptions and rethrow
            log.error("Error retrieving doctor ID from authentication context.", e);
            throw new RuntimeException("Unable to retrieve doctor ID from authentication context.", e);
        }
    }


    @GetMapping("/appointments/history")
    public ResponseEntity<List<AppointmentDto>> getHistory() {
        Long doctorId = getDoctorId();
        return ResponseEntity.ok(appointmentService.getHistory(doctorId));
    }

    @GetMapping("/appointments/today")
    public ResponseEntity<List<AppointmentDto>> getToday() {
        Long doctorId = getDoctorId();
        return ResponseEntity.ok(appointmentService.getToday(doctorId));
    }

    @GetMapping("/appointments/upcoming")
    public ResponseEntity<List<AppointmentDto>> getUpcoming() {
        Long doctorId = getDoctorId();
        return ResponseEntity.ok(appointmentService.getUpcoming(doctorId));
    }
}
