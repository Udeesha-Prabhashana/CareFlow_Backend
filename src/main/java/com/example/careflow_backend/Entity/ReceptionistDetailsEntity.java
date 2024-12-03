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
@Table(name = "Receptionist_Details")
public class ReceptionistDetailsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receptionist_id", nullable = false)
    @JsonBackReference
    private UserEntity receptionist;

    @Column(name = "shift", nullable = false)
    private String shift;

    @Column(name = "department", nullable = false)
    private String department;

    @Column(name = "experience_years", nullable = false)
    private Integer experienceYears;

    @Column(name = "description")
    private String description;
}
