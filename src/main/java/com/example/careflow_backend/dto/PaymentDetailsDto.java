package com.example.careflow_backend.dto;

import java.time.LocalDateTime;

public class PaymentDetailsDto {

    private Long appointmentId;     // The ID of the appointment (links to AppointmentEntity)
    private Long doctorId;          // The ID of the doctor (links to UserEntity)
    private LocalDateTime paymentDate;  // The date and time of payment
    private Integer amountPaid;     // The amount paid for the appointment

    // Default constructor
    public PaymentDetailsDto() {
    }

    // Parameterized constructor
    public PaymentDetailsDto(Long appointmentId, Long doctorId, LocalDateTime paymentDate, Integer amountPaid) {
        this.appointmentId = appointmentId;
        this.doctorId = doctorId;
        this.paymentDate = paymentDate;
        this.amountPaid = amountPaid;
    }

    // Getters and Setters

    public Long getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Long appointmentId) {
        this.appointmentId = appointmentId;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }

    public Integer getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(Integer amountPaid) {
        this.amountPaid = amountPaid;
    }
}
