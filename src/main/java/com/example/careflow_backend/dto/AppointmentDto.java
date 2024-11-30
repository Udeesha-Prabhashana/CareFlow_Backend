package com.example.careflow_backend.dto;

import java.time.LocalDate;

public class AppointmentDto {
    private Long id;
    private Long doctorId;
    private Long patientId;
    private LocalDate appointmentDate;
    private Integer slotNumber;
    private Integer status;

    private Integer payment;
    private String reasonForVisit;

    private String doctorName;

    private PaymentDetailsDto paymentDetails;  // New field for payment details

    // Default constructor
    public AppointmentDto() {
    }

    // Parameterized constructor
    public AppointmentDto(Long id, Long doctorId, Long patientId, LocalDate appointmentDate, Integer slotNumber, Integer status, String reasonForVisit, String doctorName, Integer payment, PaymentDetailsDto paymentDetails) {
        this.id = id;
        this.doctorId = doctorId;
        this.patientId = patientId;
        this.appointmentDate = appointmentDate;
        this.slotNumber = slotNumber;
        this.status = status;
        this.reasonForVisit = reasonForVisit;
        this.doctorName = doctorName;
        this.payment = payment;
        this.paymentDetails = paymentDetails;  // Initialize payment details
    }

    // Getters and Setters for the fields

    public Integer getPayment() {
        return payment;
    }

    public void setPayment(Integer payment) {
        this.payment = payment;
    }

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

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public PaymentDetailsDto getPaymentDetails() {
        return paymentDetails;
    }

    public void setPaymentDetails(PaymentDetailsDto paymentDetails) {
        this.paymentDetails = paymentDetails;
    }
}
