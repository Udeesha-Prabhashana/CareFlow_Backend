package com.example.careflow_backend.controller;

import com.example.careflow_backend.Entity.UserEntity;
import com.example.careflow_backend.dto.OtpVerificationDto;
import com.example.careflow_backend.dto.UserRegistrationDto;
import com.example.careflow_backend.repository.UserRepo;
import com.example.careflow_backend.service.AuthService;
import com.example.careflow_backend.service.NotificationService;
import com.example.careflow_backend.service.UserService;
import com.example.careflow_backend.utils.OtpUtils;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Slf4j
@CrossOrigin("http://localhost:3000")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;
    private final UserRepo userRepo;
    private final NotificationService notificationService;
    @PostMapping("/sign-in")
    public ResponseEntity<?> authenticateUser(Authentication authentication, HttpServletResponse response){
        return ResponseEntity.ok(authService.getJwtTokensAfterAuthentication(authentication,response));
    }

    @PreAuthorize("hasAuthority('SCOPE_REFRESH_TOKEN')")      //use to get accsess token using refresh token
    @PostMapping ("/refresh-token")
    public ResponseEntity<?> getAccessToken(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader){
        log.info("authorizationHeader ", authorizationHeader);
        return ResponseEntity.ok(authService.getAccessTokenUsingRefreshToken(authorizationHeader));
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegistrationDto userRegistrationDto,
                                          BindingResult bindingResult, HttpServletResponse httpServletResponse) {
        log.info("[AuthController:registerUser] Signup Process Started for user:{}", userRegistrationDto.userName());

        if (bindingResult.hasErrors()) {
            List<String> errorMessage = bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList();
            log.error("[AuthController:registerUser] Errors in user:{}", errorMessage);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
        }

        try {
            return ResponseEntity.ok(authService.registerUser(userRegistrationDto, httpServletResponse));
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        } catch (Exception e) {
            log.error("[AuthController:registerUser] Unexpected error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody OtpVerificationDto otpVerificationDto) {
        try {
            Optional<UserEntity> userEntityOptional = userRepo.findByMobileNumberAndOtp(
                    otpVerificationDto.mobileNumber(), otpVerificationDto.otp()
            );

            if (userEntityOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid OTP or phone number.");
            }

            UserEntity userEntity = userEntityOptional.get();

            if (userEntity.getOtpExpiry().isBefore(LocalDateTime.now())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("OTP has expired.");
            }

            userEntity.setVerified(true);
            userEntity.setOtp(null);
            userEntity.setOtpExpiry(null);
            userRepo.save(userEntity);

            return ResponseEntity.ok("OTP verified successfully. Registration complete.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Verification failed. Please try again.");
        }
    }

    @PostMapping("/resend-otp")
    public ResponseEntity<?> resendOtp(@RequestBody OtpVerificationDto otpVerificationDto) {
        Optional<UserEntity> userEntityOptional = userRepo.findByMobileNumber(otpVerificationDto.mobileNumber());

        if (userEntityOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Mobile number not found.");
        }

        UserEntity userEntity = userEntityOptional.get();

        String otp = OtpUtils.generateOtp();
        LocalDateTime otpExpiry = OtpUtils.generateOtpExpiry();

        System.out.println("Otp sending start.......1");
        userEntity.setOtp(otp);
        userEntity.setOtpExpiry(otpExpiry);
        userRepo.save(userEntity);

        System.out.println("Otp sending start.......2");

        String formattedPhoneNumber = authService.formatPhoneNumber(otpVerificationDto.mobileNumber());
        notificationService.sendOtp(formattedPhoneNumber, otp);
        return ResponseEntity.ok("OTP resent successfully.");
    }


    @PostMapping("/sign-up/doctor")
    public ResponseEntity<?> registerDoctor(@Valid @RequestBody UserRegistrationDto userRegistrationDto,
                                          BindingResult bindingResult, HttpServletResponse httpServletResponse) {
        log.info("[AuthController:registerUser] Signup Process Started for user:{}", userRegistrationDto.userName());

        if (bindingResult.hasErrors()) {
            List<String> errorMessage = bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList();
            log.error("[AuthController:registerUser] Errors in user:{}", errorMessage);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
        }

        try {
            return ResponseEntity.ok(userService.registerDoctor(userRegistrationDto, httpServletResponse));
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        } catch (Exception e) {
            log.error("[AuthController:registerUser] Unexpected error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }

    @PostMapping("/sign-up/Nurse")
    public ResponseEntity<?> registerNurse(@Valid @RequestBody UserRegistrationDto userRegistrationDto,
                                           BindingResult bindingResult, HttpServletResponse httpServletResponse) {
        log.info("[NurseController:registerNurse] Signup Process Started for nurse:{}", userRegistrationDto.userName());

        if (bindingResult.hasErrors()) {
            List<String> errorMessage = bindingResult.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .toList();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
        }

        try {
            return ResponseEntity.ok(userService.registerNurse(userRegistrationDto, httpServletResponse));
        } catch (Exception e) {
            log.error("[NurseController:registerNurse] Error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred.");
        }
    }
}

//We can get the access token using refresh token. after we logout using the that refresh token. we can not get again access token using that same refresh token