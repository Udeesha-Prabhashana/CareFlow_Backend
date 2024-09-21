package com.example.careflow_backend.dto;
import java.time.LocalDate;

public class MedicalHistoryDto {
    private Long id;
    private Long doctorId;
    private Long patientId;
    private LocalDate date;
    private String details;


    private  String doctorName;

    // Default constructor
    public MedicalHistoryDto() {
    }

    // Parameterized constructor
    public MedicalHistoryDto(Long id, Long doctorId, Long patientId, LocalDate date, String details) {
        this.id = id;
        this.doctorId = doctorId;
        this.patientId = patientId;
        this.date = date;
        this.details = details;

    }

    // Getters and Setters




    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }
}
