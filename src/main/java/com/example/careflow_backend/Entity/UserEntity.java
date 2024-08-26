package com.example.careflow_backend.Entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * Author: atquil
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString
@Table(name="Users")
public class UserEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "username")
    private String userName;

    @Column(nullable = false, name = "email", unique = true)
    private String emailId;

    @Column(name = "mobilenumber")
    private String mobileNumber;

    @Column(name = "address")
    private String address;

    @Column(name = "name")
    private String name;

    @Column(name = "specialization")
    private String specialization;

    @Column(nullable = false, name = "roles")
    private String roles;

    @Column(nullable = false, name = "password")
    private String password;

    @Column(name = "photo_url")
    private String photoUrl;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, fetch = FetchType.LAZY) // Specify cascade and fetch types
    @JsonManagedReference
    @ToString.Exclude
    private List<DoctorAvailabilityEntity> availability;

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, fetch = FetchType.LAZY) // Specify cascade and fetch types
    @JsonManagedReference
    @ToString.Exclude
    private List<DoctorDetailsEntity> doctorDetails;

    // Many-to-One relationship with RefreshTokenEntity
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference // This is the owning side of the relationship
    private List<RefreshTokenEntity> refreshTokens;
}
