package com.example.careflow_backend.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Author: atquil
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Appointments")
public class AppointmentEntity {

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

    @Column(name = "appointment_date", nullable = false)
    private LocalDate appointmentDate;

    @Column(name = "slot_number", nullable = false)
    private Integer slotNumber;

    @Column(name = "status", nullable = false)
    private Integer status;

    @Column(name = "payment", nullable = false)
    private Integer payment;

    @Column(name = "reason_for_visit")
    private String reasonForVisit;
}
