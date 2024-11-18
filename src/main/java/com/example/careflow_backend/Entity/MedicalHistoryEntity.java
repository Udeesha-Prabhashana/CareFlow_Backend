package com.example.careflow_backend.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Author: rusira
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Medical_History")
public class MedicalHistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Many-to-One relationship with UserEntity for doctor
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private UserEntity doctor;

    // Many-to-One relationship with UserEntity for patient
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private UserEntity patient;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "details", nullable = false)
    private String details;


}
