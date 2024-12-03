package com.example.careflow_backend.service;

import com.example.careflow_backend.Entity.UserEntity;
import com.example.careflow_backend.repository.ReceptionistDetailsRepo;
import com.example.careflow_backend.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class ReceptionistService {

    private final UserRepo userRepo;
    private final ReceptionistDetailsRepo receptionistDetailsRepo;

    public void deleteReceptionistById(Long userId) {
        // Check if the user exists and is a receptionist
        UserEntity user = userRepo.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Receptionist not found"));

        if (!"ROLE_RECEP".equals(user.getRoles())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Role is not a receptionist");
        }

        // Delete receptionist details first
        receptionistDetailsRepo.deleteByReceptionistId(userId);

        // Delete the user
        userRepo.deleteById(userId);
    }
}
