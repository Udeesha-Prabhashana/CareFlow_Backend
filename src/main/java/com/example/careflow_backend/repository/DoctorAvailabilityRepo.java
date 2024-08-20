package com.example.careflow_backend.repository;

import com.example.careflow_backend.Entity.DoctorAvailabilityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorAvailabilityRepo extends JpaRepository<DoctorAvailabilityEntity, Long> {

    // Find availability for a specific doctor on a specific date

    Optional<DoctorAvailabilityEntity> findByDoctorId(Long doctorId);

    Optional<DoctorAvailabilityEntity> findByDoctorIdAndAvailableDate(Long doctorId, LocalDate date);

    Optional<DoctorAvailabilityEntity> findByDoctor_IdAndAvailableDate(Long doctorId, LocalDate date);

    List<DoctorAvailabilityEntity> findByAvailableDate(LocalDate date);
}
