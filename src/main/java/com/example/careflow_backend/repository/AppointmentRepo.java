package com.example.careflow_backend.repository;
import com.example.careflow_backend.Entity.AppointmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AppointmentRepo extends JpaRepository<AppointmentEntity, Long>  {

    // Custom query methods can be defined here, e.g., find by doctor, patient, etc.
    List<AppointmentEntity> findByDoctorId(Long doctorId);

    List<AppointmentEntity> findByPatientId(Long patientId);
}
