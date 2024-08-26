package com.example.careflow_backend.dto;

public class DoctorDetailsDto {

    private Long id;
    private Long doctorId;

    private String regiNumber;
    private Double bookingCharge;

    public DoctorDetailsDto() {
    }

    public DoctorDetailsDto(Long id, Long doctorId, String registrationNumber, Double BookingCharge) {
        this.id = id;
        this.doctorId = doctorId;
        this.regiNumber = registrationNumber;
        this.bookingCharge = BookingCharge;
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

    public String getRegiNumber() {
        return regiNumber;
    }

    public void setRegiNumber(String regiNumber) {
        this.regiNumber = regiNumber;
    }

    public Double getBookingCharge() {
        return bookingCharge;
    }

    public void setBookingCharge(Double bookingCharge) {
        this.bookingCharge = bookingCharge;
    }
}
