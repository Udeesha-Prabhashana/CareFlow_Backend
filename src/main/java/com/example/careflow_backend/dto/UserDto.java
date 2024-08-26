package com.example.careflow_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
public class UserDto {
    private Long id;
    private String userName;
    private String emailId;
    private String mobileNumber;
    private String address;
    private String name;
    private String specialization;
    private String roles;
    private String photoUrl;
    private LocalDate availableDate;
    private Integer bookedSlots;
    private String description;
    private List<DoctorAvailabilityDto> availability;
    private Double docCharge;

    @Data
    @AllArgsConstructor
    public static class DoctorAvailabilityDto {
        private LocalDate availableDate;
        private int bookedSlots;
    }

    // Constructor without availability details
    public UserDto(Long id, String userName, String emailId, String mobileNumber, String address, String name, String specialization, String roles, String photoUrl , String description, Double docCharge) {
        this.id = id;
        this.userName = userName;
        this.emailId = emailId;
        this.mobileNumber = mobileNumber;
        this.address = address;
        this.name = name;
        this.specialization = specialization;
        this.roles = roles;
        this.photoUrl = photoUrl;
        this.description =description;
        this.docCharge = docCharge;
    }

    // Constructor with availability details
    public UserDto(Long id, String userName, String emailId, String mobileNumber, String address, String name, String specialization, String roles, String photoUrl, LocalDate availableDate, Integer bookedSlots,String description, List<DoctorAvailabilityDto> availability , Double docCharge) {
        this.id = id;
        this.userName = userName;
        this.emailId = emailId;
        this.mobileNumber = mobileNumber;
        this.address = address;
        this.name = name;
        this.specialization = specialization;
        this.roles = roles;
        this.photoUrl = photoUrl;
        this.availableDate = availableDate;
        this.bookedSlots = bookedSlots;
        this.description=description;
        this.availability = availability;
        this.docCharge = docCharge;
    }

    public UserDto(Long id, String userName, String emailId, String mobileNumber, String address, String name, String specialization, String roles, String photoUrl, LocalDate availableDate, Integer bookedSlots, String description ,Double docCharge) {
        this.id = id;
        this.userName = userName;
        this.emailId = emailId;
        this.mobileNumber = mobileNumber;
        this.address = address;
        this.name = name;
        this.specialization = specialization;
        this.roles = roles;
        this.photoUrl = photoUrl;
        this.availableDate = availableDate;
        this.bookedSlots = bookedSlots;
        this.description = description;
        this.docCharge = docCharge;
    }

    // New Constructor to match your current usage
    public UserDto(Long id, String userName, String emailId, String mobileNumber, String address, String name, String specialization, String roles,String photoUrl,String description, List<DoctorAvailabilityDto> availability , Double docCharge) {
        this.id = id;
        this.userName = userName;
        this.emailId = emailId;
        this.mobileNumber = mobileNumber;
        this.address = address;
        this.name = name;
        this.specialization = specialization;
        this.roles = roles;
        this.photoUrl = photoUrl;
        this.description = description;
        this.availability = availability;
        this.docCharge = docCharge;
    }
}
