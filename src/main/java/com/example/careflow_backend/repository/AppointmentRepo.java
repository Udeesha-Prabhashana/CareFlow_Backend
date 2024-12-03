package com.example.careflow_backend.repository;
import com.example.careflow_backend.Entity.AppointmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.time.LocalDate;

public interface AppointmentRepo extends JpaRepository<AppointmentEntity, Long>  {

    // Custom query methods can be defined here, e.g., find by doctor, patient, etc.
    List<AppointmentEntity> findByDoctorId(Long doctorId);

    List<AppointmentEntity> findByPatientId(Long patientId);

    int countByDoctorIdAndStatus(Long doctorId, Integer status);

    @Query("SELECT a FROM AppointmentEntity a WHERE a.isCancelled = false")
    List<AppointmentEntity> findAllActiveAppointments();

    List<AppointmentEntity> findByPatientIdAndIsCancelledFalse(Long patientId);


    @Query("SELECT a FROM AppointmentEntity a WHERE a.doctor.id = :doctorId AND a.status = 1 AND a.appointmentDate < CURRENT_DATE")
    List<AppointmentEntity> findHistory(Long doctorId);

    @Query("SELECT a FROM AppointmentEntity a WHERE a.doctor.id = :doctorId AND a.appointmentDate = CURRENT_DATE AND (a.status = 0 OR a.status = 1)")
    List<AppointmentEntity> findToday(Long doctorId);

    @Query("SELECT a FROM AppointmentEntity a WHERE a.doctor.id = :doctorId AND a.status = 0 AND a.appointmentDate > CURRENT_DATE")
    List<AppointmentEntity> findUpcoming(Long doctorId);

}
