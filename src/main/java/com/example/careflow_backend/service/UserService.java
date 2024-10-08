package com.example.careflow_backend.service;

import com.example.careflow_backend.Entity.DoctorDetailsEntity;
import com.example.careflow_backend.Entity.UserEntity;
import com.example.careflow_backend.dto.UserDto;
import com.example.careflow_backend.dto.UserRegistrationDto;
import com.example.careflow_backend.mapper.EntityMapper;
import com.example.careflow_backend.repository.DoctorDetailsRepo;
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
    private final EntityMapper entityMapper;

    public UserService(UserRepo userInfoRepo, DoctorDetailsRepo doctorDetailsRepo, EntityMapper entityMapper) {
        this.userInfoRepo = userInfoRepo;
        this.doctorDetailsRepo = doctorDetailsRepo;
        this.entityMapper = entityMapper;
    }

    public List<UserDto> getUsersByRole(String role) {
        try {
            log.info("[AuthService:getUsersByRole] Fetching users with role: {}", role);

            // Fetch users by role
            List<UserDto> users = userInfoRepo.findByRoles(role);

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

}
