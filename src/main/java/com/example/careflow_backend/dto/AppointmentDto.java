package com.example.careflow_backend.dto;
import java.time.LocalDate;

public class AppointmentDto {
    private Long id;
    private Long doctorId;
    private Long patientId;
    private LocalDate appointmentDate;
    private Integer slotNumber;
    private Integer status;
    private String reasonForVisit;

    // Default constructor
    public AppointmentDto() {
    }

    // Parameterized constructor
    public AppointmentDto(Long id, Long doctorId, Long patientId, LocalDate appointmentDate, Integer slotNumber, Integer status, String reasonForVisit) {
        this.id = id;
        this.doctorId = doctorId;
        this.patientId = patientId;
        this.appointmentDate = appointmentDate;
        this.slotNumber = slotNumber;
        this.status = status;
        this.reasonForVisit = reasonForVisit;
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

    public LocalDate getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(LocalDate appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public Integer getSlotNumber() {
        return slotNumber;
    }

    public void setSlotNumber(Integer slotNumber) {
        this.slotNumber = slotNumber;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getReasonForVisit() {
        return reasonForVisit;
    }

    public void setReasonForVisit(String reasonForVisit) {
        this.reasonForVisit = reasonForVisit;
    }
}
