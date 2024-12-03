package com.example.careflow_backend.service;

import com.example.careflow_backend.Entity.UserEntity;
import com.example.careflow_backend.repository.NurseDetailsRepo;
import com.example.careflow_backend.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class NurseService {

    private final UserRepo userRepo;
    private final NurseDetailsRepo nurseDetailsRepo;
    public void deleteNurseById(Long userId) {
        // Check if the user exists and is a doctor
        UserEntity user = userRepo.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Nurse not found"));

        if (!"ROLE_NURSE".equals(user.getRoles())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Role is not a nurse");
        }

        // Delete doctor details first
        nurseDetailsRepo.deleteByNurseId(userId);

        // Delete the user
        userRepo.deleteById(userId);
    }
}
