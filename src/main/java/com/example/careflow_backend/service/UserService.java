package com.example.careflow_backend.service;

import com.example.careflow_backend.Entity.UserEntity;
import com.example.careflow_backend.dto.UserDto;
import com.example.careflow_backend.repository.UserRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
@Slf4j
@Service

public class UserService {

    private final UserRepo userInfoRepo;

    public UserService(UserRepo userInfoRepo) {
        this.userInfoRepo = userInfoRepo;
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

}
