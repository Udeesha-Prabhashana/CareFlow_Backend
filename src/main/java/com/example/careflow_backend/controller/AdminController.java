package com.example.careflow_backend.controller;

import com.example.careflow_backend.Entity.UserEntity;
import com.example.careflow_backend.dto.UserDto;
import com.example.careflow_backend.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin("http://localhost:3000")
public class AdminController {

    @Autowired
    private UserService userService; // Ensure this is not static

    @PreAuthorize("hasRole('ROLE_ADMIN')")
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
}
