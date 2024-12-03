package com.example.careflow_backend.service;

import com.example.careflow_backend.Entity.DoctorDetailsEntity;
import com.example.careflow_backend.Entity.NurseDetailsEntity;
import com.example.careflow_backend.Entity.UserEntity;
import com.example.careflow_backend.dto.UserDto;
import com.example.careflow_backend.dto.UserRegistrationDto;
import com.example.careflow_backend.mapper.EntityMapper;
import com.example.careflow_backend.repository.DoctorDetailsRepo;
import com.example.careflow_backend.repository.NurseDetailsRepo;
import com.example.careflow_backend.repository.UserRepo;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserService {

    private final UserRepo userInfoRepo;
    private final DoctorDetailsRepo doctorDetailsRepo;

    private final NurseDetailsRepo nurseDetailsRepo;
    private final EntityMapper entityMapper;
    private final UserRepo userRepo;

    public UserService(UserRepo userInfoRepo, DoctorDetailsRepo doctorDetailsRepo, NurseDetailsRepo nurseDetailsRepo, EntityMapper entityMapper, UserRepo userRepo) {
        this.userInfoRepo = userInfoRepo;
        this.doctorDetailsRepo = doctorDetailsRepo;
        this.nurseDetailsRepo = nurseDetailsRepo;
        this.entityMapper = entityMapper;
        this.userRepo = userRepo;
    }

    public List<UserDto> getUsersByRole(String role) {
        try {
            log.info("[AuthService:getUsersByRole] Fetching users with role: {}", role);
            List<UserDto> users;
            if ("ROLE_DOCTOR".equalsIgnoreCase(role)) {
                // Fetch detailed doctor data
                users = userRepo.findDoctorsByRole(role);
            } else if("ROLE_NURSE".equalsIgnoreCase(role)) {
                // Fetch general user data
                users = userRepo.findNurseByRole(role);
            }else{
                users = userRepo.findUsersByRole(role);
            }

            if (users.isEmpty()) {
                log.info("[AuthService:getUsersByRole] No users found with role: {}", role);
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No users found with the specified role.");
            }

            log.info("[AuthService:getUsersByRole] Found {} users with role: {}", users.size(), role);
            return users;

        } catch (Exception e) {
            log.error("[AuthService:getUsersByRole] Unexpected error: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred", e);
        }
    }

//    public List<UserDto> getUsersByRoleUser(String role) {
//        try {
//            log.info("[AuthService:getUsersByRole] Fetching users with role: {}", role);
//
//            // Fetch users by role
//            List<UserDto> users = userInfoRepo.findByRoles(role);
//
//            if (users.isEmpty()) {
//                log.info("[AuthService:getUsersByRole] No users found with role: {}", role);
//                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No users found with the specified role.");
//            }
//
//            log.info("[AuthService:getUsersByRole] Found {} users with role: {}", users.size(), role);
//            return users;
//
//        } catch (Exception e) {
//            log.error("[AuthService:getUsersByRole] Unexpected error: {}", e.getMessage());
//            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred", e);
//        }
//    }

    public String registerDoctor(UserRegistrationDto userRegistrationDto, HttpServletResponse httpServletResponse) {
        try {
            log.info("[AuthService:registerUser] User Registration Started with :::{}", userRegistrationDto);

            // Check if email already exists
            Optional<UserEntity> existingUserByEmail = userInfoRepo.findByEmailId(userRegistrationDto.userEmail());
            if (existingUserByEmail.isPresent()) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Email is already registered.");
            }

            // Check if mobile number already exists
            Optional<UserEntity> existingUserByMobile = userInfoRepo.findByMobileNumber(userRegistrationDto.userMobileNo());
            if (existingUserByMobile.isPresent()) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Mobile number is already registered.");
            }

            // Proceed with registration
            UserEntity userDetailsEntity = entityMapper.convertToUserEntity(userRegistrationDto);

            UserEntity savedUserDetails = userInfoRepo.save(userDetailsEntity);

            DoctorDetailsEntity doctorDetailsEntity = entityMapper.convertDoctorDetailsEntity(savedUserDetails , userRegistrationDto );
//            log.info("doctorDetailsEntity Entity: {}", doctorDetailsEntity);
            DoctorDetailsEntity savedDoctorDetails =  doctorDetailsRepo.save(doctorDetailsEntity);
            log.info("[AuthService:registerDoctor] User:{} Successfully registered", savedUserDetails.getUserName());

            return "Doctor added successfully";

        } catch (ResponseStatusException e) {
            log.error("[AuthService:registerUser] Registration error: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("[AuthService:registerUser] Unexpected error: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred", e);
        }
    }

    public String registerNurse(UserRegistrationDto userRegistrationDto, HttpServletResponse httpServletResponse) {
        try {
            log.info("[AuthService:registerNurse] Nurse Registration Started with :::{}", userRegistrationDto);

            // Check if email already exists
            Optional<UserEntity> existingUserByEmail = userInfoRepo.findByEmailId(userRegistrationDto.userEmail());
            if (existingUserByEmail.isPresent()) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Email is already registered.");
            }

            // Check if mobile number already exists
            Optional<UserEntity> existingUserByMobile = userInfoRepo.findByMobileNumber(userRegistrationDto.userMobileNo());
            if (existingUserByMobile.isPresent()) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Mobile number is already registered.");
            }

            // Proceed with registration
            UserEntity userDetailsEntity = entityMapper.convertToUserEntity(userRegistrationDto);

            UserEntity savedUserDetails = userInfoRepo.save(userDetailsEntity);

            NurseDetailsEntity nurseDetailsEntity = entityMapper.convertNurseDetailsEntity(savedUserDetails, userRegistrationDto);
            NurseDetailsEntity savedNurseDetails = nurseDetailsRepo.save(nurseDetailsEntity);

            log.info("[AuthService:registerNurse] Nurse:{} Successfully registered", savedUserDetails.getUserName());

            return "Nurse added successfully";

        } catch (ResponseStatusException e) {
            log.error("[AuthService:registerNurse] Registration error: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("[AuthService:registerNurse] Unexpected error: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred", e);
        }
    }


    public UserDto getUserById(Long id) {
        UserEntity userEntity = userRepo.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with id: " + id)
        );
        return new UserDto(userEntity.getName(), userEntity.getMobileNumber(), userEntity.getEmailId(), userEntity.getPhotoUrl());
    }

    public void updateUserProfile(Long userId, UserDto userDto) {
        UserEntity userEntity = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Update fields
        userEntity.setName(userDto.getName());
        userEntity.setEmailId(userDto.getEmailId());
        userEntity.setMobileNumber(userDto.getMobileNumber());
        userEntity.setPhotoUrl(userDto.getPhotoUrl());

        userRepo.save(userEntity);
    }




}
