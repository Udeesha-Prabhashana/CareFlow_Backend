package com.example.careflow_backend.dto;

import com.example.careflow_backend.repository.DoctorDetailsRepo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class AppointmentDetailsDto {
    private Long appointmentId;
    private LocalDate appointmentDate;
    private Integer slotNumber;
    private String reasonForVisit;
    private Double payment;
    private Long doctorId;
    private String doctorName;
}
