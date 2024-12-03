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
    private String availableTime;
    private Integer bookedSlots;
    private Integer totalSlots;
    private String description;
    private List<DoctorAvailabilityDto> availability;
    private Double docCharge;
    private String registrationNumber;

    @Data
    @AllArgsConstructor
    public static class DoctorAvailabilityDto {
        private LocalDate availableDate;
        private String availableTime;
        private int bookedSlots;
        private int TotalSlots;
    }

    // Constructor without availability details
    public UserDto(Long id, String userName, String emailId, String mobileNumber, String address, String name, String specialization, String roles, String photoUrl , String description, Double docCharge, String registrationNumber) {
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
        this.registrationNumber = registrationNumber;
    }

    public UserDto(Long id, String userName, String emailId, String mobileNumber, String address, String name, String roles, String photoUrl) {
        this.id = id;
        this.userName = userName;
        this.emailId = emailId;
        this.mobileNumber = mobileNumber;
        this.address = address;
        this.name = name;
        this.roles = roles;
        this.photoUrl = photoUrl;
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

    public UserDto(Long id, String userName, String emailId, String mobileNumber, String address, String name, String specialization, String roles, String photoUrl, LocalDate availableDate, String availableTime, Integer totalSlots, Integer bookedSlots, String description ,Double docCharge) {
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
        this.availableTime =availableTime;
        this.totalSlots = totalSlots;
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

//    public UserDto(String name, String mobileNumber, String emailId, String photoUrl) {
//        this.name = name;
//        this.mobileNumber = mobileNumber;
//        this.emailId = emailId;
//        this.photoUrl = photoUrl;
//    }

    public UserDto(String name, String mobileNumber, String emailId, String photoUrl, String specialization, String description) {
        this.name = name;
        this.mobileNumber = mobileNumber;
        this.emailId = emailId;
        this.photoUrl = photoUrl;
        this.specialization = specialization;
        this.description = description;
    }

}
