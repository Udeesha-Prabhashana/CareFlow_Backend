package com.example.careflow_backend.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Nurse_Details")
public class NurseDetailsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nurse_id", nullable = false)
    @JsonBackReference
    private UserEntity nurse;

    @Column(name = "registration_number", nullable = false)
    private String registrationNumber;

    @Column(name = "specialization", nullable = false)
    private String specialization;

    @Column(name = "department", nullable = false)
    private String department;

    @Column(name = "experience_years", nullable = false)
    private Integer experienceYears;

    @Column(name = "description")
    private String description;
}
