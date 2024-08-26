package com.example.careflow_backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public record UserRegistrationDto (
        @NotEmpty(message = "User Name must not be empty")
        String userName,
        String userMobileNo,

        String name,
        String userAddress,

        String description,
        String registrationNumber,

        Double BookingCharge,
        String userSpecialization,
        @NotEmpty(message = "User email must not be empty") //Neither null nor 0 size
        @Email(message = "Invalid email format")
        String userEmail,

        @NotEmpty(message = "User password must not be empty")
        String userPassword,
        @NotEmpty(message = "User role must not be empty")
        String userRole
){ }
