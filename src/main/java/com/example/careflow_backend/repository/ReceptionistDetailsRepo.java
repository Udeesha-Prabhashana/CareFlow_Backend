package com.example.careflow_backend.repository;

import com.example.careflow_backend.Entity.ReceptionistDetailsEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReceptionistDetailsRepo extends JpaRepository<ReceptionistDetailsEntity, Long> {

    @Modifying
    @Transactional
    @Query("DELETE FROM ReceptionistDetailsEntity d WHERE d.receptionist.id = :receptionistId")
    void deleteByReceptionistId(@Param("receptionistId") Long receptionistId);

}
