package com.example.careflow_backend.repository;

import com.example.careflow_backend.Entity.DoctorDetailsEntity;
import com.example.careflow_backend.Entity.NurseDetailsEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NurseDetailsRepo extends JpaRepository<NurseDetailsEntity, Long> {

    NurseDetailsEntity findByNurseId(Long doctorId);
    @Modifying
    @Transactional
    @Query("DELETE FROM NurseDetailsEntity d WHERE d.nurse.id = :nurseId")
    void deleteByNurseId(@Param("nurseId") Long nurseId);
}
