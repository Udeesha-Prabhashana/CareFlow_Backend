package com.example.careflow_backend.repository;

import com.example.careflow_backend.Entity.DoctorDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorDetailsRepo extends JpaRepository<DoctorDetailsEntity , Long> {

}
