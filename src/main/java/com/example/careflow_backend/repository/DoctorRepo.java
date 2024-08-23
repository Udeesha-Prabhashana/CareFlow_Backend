package com.example.careflow_backend.repository;

import com.example.careflow_backend.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DoctorRepo extends JpaRepository<UserEntity, Long> {

    // Find doctors based on role, name, specialization, and availability date
    List<UserEntity> findByRolesAndNameContainingIgnoreCaseAndSpecializationContainingIgnoreCaseAndAvailability_AvailableDate(
            String roles,
            String name,
            String specialization,
            LocalDate availableDate
    );

    // Handle cases where some filters might be null
    List<UserEntity> findByRolesAndNameContainingIgnoreCaseAndSpecializationContainingIgnoreCaseAndAvailability_AvailableDateIsNull(
            String roles,
            String name,
            String specialization
    );

    List<UserEntity> findByRolesAndNameContainingIgnoreCaseAndSpecializationContainingIgnoreCaseAndAvailabilityIsNull(
            String roles,
            String name,
            String specialization
    );
}
