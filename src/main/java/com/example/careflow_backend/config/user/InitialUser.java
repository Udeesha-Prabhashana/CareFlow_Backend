package com.example.careflow_backend.config.user;

import com.example.careflow_backend.Entity.UserEntity;
import com.example.careflow_backend.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
@Slf4j
public class InitialUser {     //When use CommandLineRunner run this class initially when start run this application
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    public void run(String... args) throws Exception {
        UserEntity manager = new UserEntity();
        manager.setUserName("Manager");
        manager.setPassword(passwordEncoder.encode("password"));
        manager.setRoles("ROLE_MANAGER");
        manager.setEmailId("manager@manager.com");

        UserEntity admin = new UserEntity();
        admin.setUserName("Admin");
        admin.setPassword(passwordEncoder.encode("password"));
        admin.setRoles("ROLE_ADMIN");
        admin.setEmailId("admin@admin.com");

        UserEntity user = new UserEntity();
        user.setUserName("User");
        user.setPassword(passwordEncoder.encode("password"));
        user.setRoles("ROLE_USER");
        user.setEmailId("user@user.com");

        userRepo.saveAll(List.of(manager,admin,user));
    }

}
