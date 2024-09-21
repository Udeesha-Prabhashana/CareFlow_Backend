package com.example.careflow_backend.repository;
import com.example.careflow_backend.Entity.AppointmentEntity;
import com.example.careflow_backend.Entity.MedicalHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedicalHistoryRepo extends JpaRepository<MedicalHistoryEntity, Long>  {

    // Custom query methods can be defined here, e.g., find by doctor, patient, etc.
    List<MedicalHistoryEntity> findByDoctorId(Long doctorId);

    List<MedicalHistoryEntity> findByPatientId(Long patientId);
}
