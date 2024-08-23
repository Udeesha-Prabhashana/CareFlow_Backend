package com.example.careflow_backend.dto;

import java.time.LocalDate;

public class DoctorFilterDto {

    private String roles;
    private String name;
    private String specialization;
    private LocalDate date;

    public DoctorFilterDto(String roles, String name, String specialization, LocalDate date) {
        this.roles = roles;
        this.name = name;
        this.specialization = specialization;
        this.date = date;
    }

    public DoctorFilterDto() {

    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
