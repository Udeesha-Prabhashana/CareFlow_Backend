package com.example.careflow_backend.controller;

import com.example.careflow_backend.config.jwtConfig.JwtTokenUtils;
import com.example.careflow_backend.dto.PaymentDetailsDto;
import com.example.careflow_backend.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api")
@CrossOrigin("http://localhost:3000")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;
    private final JwtTokenUtils jwtTokenUtils;

    @PostMapping("payment/add")
    public ResponseEntity<String> addPayment(@RequestBody PaymentDetailsDto paymentDetailsDto) {
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
                paymentService.addPayment(paymentDetailsDto , userId);
                return ResponseEntity.ok("Payment added successfully and appointment updated.");
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
}
