package com.example.careflow_backend.controller;

import com.example.careflow_backend.Entity.DoctorEntity;
import com.example.careflow_backend.Entity.UserEntity;
import com.example.careflow_backend.config.jwtConfig.JwtTokenUtils;
import com.example.careflow_backend.dto.AppointmentDto;
import com.example.careflow_backend.dto.DoctorFilterDto;
import com.example.careflow_backend.dto.UserDto;
import com.example.careflow_backend.service.DoctorService;
import com.example.careflow_backend.service.NurseService;
import com.example.careflow_backend.service.ReceptionistService;
import com.example.careflow_backend.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping({"/api", "/"})
@RequiredArgsConstructor
@Slf4j
@CrossOrigin("http://localhost:3000")
public class UserController {

    @Autowired
    private UserService userService; // Ensure this is not static
    private final DoctorService doctorService;
    private final NurseService nurseService;
    private final ReceptionistService receptionistService;
    private final JwtTokenUtils jwtTokenUtils;

//    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @GetMapping("/Doctors")
    public ResponseEntity<List<UserDto>> getDoctors() {
        try {
            String role = "ROLE_DOCTOR";
            List<UserDto> users = userService.getUsersByRole(role);
            return ResponseEntity.ok(users);
        } catch (ResponseStatusException e) {
            // Return an empty list and appropriate status code
            log.error("[UserController:getDoctors] Error occurred: {}", e.getMessage());
            return ResponseEntity.status(e.getStatusCode()).body(Collections.emptyList());
        } catch (Exception e) {
            // Return an empty list and internal server error
            log.error("[UserController:getDoctors] Unexpected error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
        }
    }

    @GetMapping("/Patients")
    public ResponseEntity<List<UserDto>> getPatients() {
        try {
            String role = "ROLE_USER";
            List<UserDto> users = userService.getUsersByRole(role);
            return ResponseEntity.ok(users);
        } catch (ResponseStatusException e) {
            // Return an empty list and appropriate status code
            log.error("[UserController:getDoctors] Error occurred: {}", e.getMessage());
            return ResponseEntity.status(e.getStatusCode()).body(Collections.emptyList());
        } catch (Exception e) {
            // Return an empty list and internal server error
            log.error("[UserController:getDoctors] Unexpected error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
        }
    }
    @GetMapping("/Receptionists")
    public ResponseEntity<List<UserDto>> getReceptionists() {
        try {
            String role = "ROLE_RECEP";
            List<UserDto> users = userService.getUsersByRole(role);
            return ResponseEntity.ok(users);
        } catch (ResponseStatusException e) {
            // Return an empty list and appropriate status code
            log.error("[UserController:getDoctors] Error occurred: {}", e.getMessage());
            return ResponseEntity.status(e.getStatusCode()).body(Collections.emptyList());
        } catch (Exception e) {
            // Return an empty list and internal server error
            log.error("[UserController:getDoctors] Unexpected error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
        }
    }

    @GetMapping("/Nurses")
    public ResponseEntity<List<UserDto>> getPNurse() {
        try {
            String role = "ROLE_NURSE";
            List<UserDto> users = userService.getUsersByRole(role);
            return ResponseEntity.ok(users);
        } catch (ResponseStatusException e) {
            // Return an empty list and appropriate status code
            log.error("[UserController:getDoctors] Error occurred: {}", e.getMessage());
            return ResponseEntity.status(e.getStatusCode()).body(Collections.emptyList());
        } catch (Exception e) {
            // Return an empty list and internal server error
            log.error("[UserController:getDoctors] Unexpected error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
        }
    }

    @GetMapping("/getAllFilteredDoctors")
    public ResponseEntity<List<UserDto>> getAllFilteredDoctors(@RequestParam(required = false) String roles,
                                                                    @RequestParam(required = false) String name,
                                                                    @RequestParam(required = false) String specialization,
                                                                    @RequestParam(required = false) String date) {

        DoctorFilterDto filterDto = new DoctorFilterDto();
        filterDto.setRoles(roles);
        filterDto.setName(name);
        filterDto.setSpecialization(specialization);

        // Parse the date if provided, otherwise set it to null
        if (date != null && !date.isEmpty()) {
            try {
                filterDto.setDate(LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            } catch (DateTimeParseException e) {
                return ResponseEntity.badRequest().body(null);
            }
        }

        System.out.println("DoctorFilerDTO" + filterDto.getName() + filterDto.getRoles() + filterDto.getSpecialization() + filterDto.getDate());

        List<UserDto> filteredDoctors = doctorService.getFilteredDoctors(filterDto);
        return ResponseEntity.ok(filteredDoctors);
    }

    @GetMapping("/getUserById")
    public ResponseEntity<UserDto> getUserById() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            System.out.println("Auth: " + authentication);

            if (authentication != null && authentication.getPrincipal() instanceof Jwt) {
                Jwt jwt = (Jwt) authentication.getPrincipal();
                // Extract user ID from JWT token
                Long userId = jwtTokenUtils.getUserId(jwt);

                if (userId != null) {
                    System.out.println("UserID: " + userId);
                    // Pass the userId to your service method
                    UserDto userDto = userService.getUserById(userId);
                    return ResponseEntity.ok(userDto);
                } else {
                    System.out.println("UserID is null");
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null); // Corrected
                }
            } else {
                System.out.println("Principal is not an instance of Jwt");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null); // Corrected
            }
        } catch (ResponseStatusException e) {
            // Log the exception and return an error response
            log.error("Error while fetching appointments", e);
            return ResponseEntity.status(e.getStatusCode()).body(null); // Corrected
        } catch (Exception e) {
            // Return an empty list and internal server error
            log.error("[UserController:GetAllAppointments] Unexpected error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // Corrected
        }
    }

    @PostMapping("/api/updateUserProfile")
    public ResponseEntity<String> updateUserProfile(@RequestBody UserDto userDto, @AuthenticationPrincipal Jwt jwt) {
        Long userId = jwtTokenUtils.getUserId(jwt);
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User ID not found in token.");
        }
        userService.updateUserProfile(userId, userDto);
        return ResponseEntity.ok("Profile updated successfully.");
    }

    @PostMapping("/Doctors/{userId}")
    public ResponseEntity<String> deleteDoctor(@PathVariable Long userId) {
        try {
            doctorService.deleteDoctorById(userId); // Assuming this method handles deletion
            return ResponseEntity.ok("Doctor deleted successfully");
        } catch (ResponseStatusException e) {
            // Handle specific exceptions that provide a meaningful status and message
            log.error("Error deleting doctor: {}", e.getReason());
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        } catch (Exception e) {
            // Handle generic exceptions
            log.error("Unexpected error while deleting doctor: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
    }

    @PostMapping("/Nurses/{userId}")
    public ResponseEntity<String> deleteNurse(@PathVariable Long userId) {
        try {
            nurseService.deleteNurseById(userId); // Assuming this method handles deletion
            return ResponseEntity.ok("Nurse deleted successfully");
        } catch (ResponseStatusException e) {
            // Handle specific exceptions that provide a meaningful status and message
            log.error("Error deleting doctor: {}", e.getReason());
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        } catch (Exception e) {
            // Handle generic exceptions
            log.error("Unexpected error while deleting doctor: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
    }

    @GetMapping("getPatientById/{id}")
    public ResponseEntity<UserDto> getPatientById(@PathVariable Long id) {
        UserDto userDto = userService.getUserById(id);
        return ResponseEntity.ok(userDto);
    }

    @PostMapping("/Receptionists/{userId}")
    public ResponseEntity<String> deleteReceptionists(@PathVariable Long userId) {
        try {
            receptionistService.deleteReceptionistById(userId); // Assuming this method handles deletion
            return ResponseEntity.ok("Receptionist deleted successfully");
        } catch (ResponseStatusException e) {
            // Handle specific exceptions that provide a meaningful status and message
            log.error("Error deleting doctor: {}", e.getReason());
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        } catch (Exception e) {
            // Handle generic exceptions
            log.error("Unexpected error while deleting doctor: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
    }
}
