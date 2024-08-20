package com.example.careflow_backend.dto;

import java.time.LocalDate;

public class DoctorAvailabilityDto {

    private Long id;
    private Long doctorId;
    private LocalDate date;

    private String available_time;
    private Integer totalSlots;
    private Integer bookedSlots;

    // Default constructor
    public DoctorAvailabilityDto() {
    }

    // Parameterized constructor
    public DoctorAvailabilityDto(Long id, Long doctorId, LocalDate date, Integer totalSlots, String available_time, Integer bookedSlots) {
        this.id = id;
        this.doctorId = doctorId;
        this.date = date;
        this.available_time = available_time;
        this.totalSlots = totalSlots;
        this.bookedSlots = bookedSlots;
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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getAvailable_time() {
        return available_time;
    }

    public void setAvailable_time(String available_time) {
        this.available_time = available_time;
    }

    public Integer getTotalSlots() {
        return totalSlots;
    }

    public void setTotalSlots(Integer totalSlots) {
        this.totalSlots = totalSlots;
    }

    public Integer getBookedSlots() {
        return bookedSlots;
    }

    public void setBookedSlots(Integer bookedSlots) {
        this.bookedSlots = bookedSlots;
    }
}

