package com.example.careflow_backend.controller;

import com.example.careflow_backend.Entity.DoctorEntity;
import com.example.careflow_backend.Entity.UserEntity;
import com.example.careflow_backend.dto.DoctorFilterDto;
import com.example.careflow_backend.dto.UserDto;
import com.example.careflow_backend.service.DoctorService;
import com.example.careflow_backend.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin("http://localhost:3000")
public class UserController {

    @Autowired
    private UserService userService; // Ensure this is not static
    private final DoctorService doctorService;

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
}
