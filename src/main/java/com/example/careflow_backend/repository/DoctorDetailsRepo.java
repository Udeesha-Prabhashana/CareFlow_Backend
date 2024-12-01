package com.example.careflow_backend.repository;

import com.example.careflow_backend.Entity.DoctorDetailsEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DoctorDetailsRepo extends JpaRepository<DoctorDetailsEntity , Long> {
    DoctorDetailsEntity findByDoctorId(Long doctorId);
    @Modifying
    @Transactional
    @Query("DELETE FROM DoctorDetailsEntity d WHERE d.doctor.id = :doctorId")
    void deleteByDoctorId(@Param("doctorId") Long doctorId);

}
