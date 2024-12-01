package com.example.careflow_backend.Entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class PaymentDetailsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "appointment_id", unique = true)
    private AppointmentEntity appointment;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private UserEntity doctor;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private UserEntity patient;

    private LocalDateTime paymentDate;
    private Integer amountPaid;

    // Default constructor
    public PaymentDetailsEntity() {
    }

    // Parameterized constructor
    public PaymentDetailsEntity(AppointmentEntity appointment, UserEntity doctor, UserEntity patient, LocalDateTime paymentDate, Integer amountPaid) {
        this.appointment = appointment;
        this.doctor = doctor;
        this.patient = patient;
        this.paymentDate = paymentDate;
        this.amountPaid = amountPaid;
    }

    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AppointmentEntity getAppointment() {
        return appointment;
    }

    public void setAppointment(AppointmentEntity appointment) {
        this.appointment = appointment;
    }

    public UserEntity getDoctor() {
        return doctor;
    }

    public void setDoctor(UserEntity doctor) {
        this.doctor = doctor;
    }

    public UserEntity getPatient() {
        return patient;
    }

    public void setPatient(UserEntity patient) {
        this.patient = patient;
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
