package com.example.careflow_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String userName;
    private String emailId;
    private String mobileNumber;
    private String address;
    private String name;
    private String specialization;
    private String roles;
}
