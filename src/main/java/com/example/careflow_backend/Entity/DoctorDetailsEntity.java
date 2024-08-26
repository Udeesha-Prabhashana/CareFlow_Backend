package com.example.careflow_backend.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Doctor_Details")
public class DoctorDetailsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    @JsonBackReference
    @JsonIgnore
    private UserEntity doctor;

    @Column(name = "registartion_number", nullable = false)
    private String registrationNumber;

    @Column(name = "booking_charge", nullable = false)
    private Double BookingCharge;

    @Column(name = "specialization", nullable = false)
    private String specialization;

    @Column(name = "description")
    private String description;

}
