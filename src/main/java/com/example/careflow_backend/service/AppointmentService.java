package com.example.careflow_backend.service;

import com.example.careflow_backend.Entity.AppointmentEntity;
import com.example.careflow_backend.Entity.DoctorAvailabilityEntity;
import com.example.careflow_backend.Entity.UserEntity;
import com.example.careflow_backend.dto.AppointmentDto;
import com.example.careflow_backend.repository.AppointmentRepo;
import com.example.careflow_backend.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppointmentService {
    private final AppointmentRepo appointmentRepository;
    private final UserRepo userRepository;
    private final DoctorAvailabilityService doctorAvailabilityService;

    public AppointmentDto addAppointment(AppointmentDto appointmentDto , Long userId) {
        // Convert DTO to entity
        AppointmentEntity appointmentEntity = new AppointmentEntity();
        appointmentEntity.setDoctor(userRepository.findById(appointmentDto.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Doctor not found")));
        appointmentEntity.setPatient(userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Patient not found")));
        appointmentEntity.setAppointmentDate(appointmentDto.getAppointmentDate());
        appointmentEntity.setSlotNumber(appointmentDto.getSlotNumber());
        appointmentEntity.setStatus(appointmentDto.getStatus());
        appointmentEntity.setReasonForVisit(appointmentDto.getReasonForVisit());
        appointmentEntity.setPayment(appointmentDto.getPayment());
        // Retrieve Doctor Availability for the given date
        DoctorAvailabilityEntity availability = doctorAvailabilityService.getAvailabilityForDoctorOnDate(
                appointmentDto.getDoctorId(), appointmentDto.getAppointmentDate());

        System.out.println("Availability" + availability);

        // Check if slots are available
        if (availability != null && availability.getBookedSlots() < availability.getTotalSlots() && availability.getBookedSlots() +1 == appointmentDto.getSlotNumber()) {
            // Increment the booked slots and save the availability
            availability.setBookedSlots(availability.getBookedSlots() + 1);
            doctorAvailabilityService.saveAvailability(availability);

            // Save the appointment entity to the database
            AppointmentEntity savedAppointment = appointmentRepository.save(appointmentEntity);

            // Convert the saved entity back to DTO
            appointmentDto.setId(savedAppointment.getId());
            return appointmentDto;
        } else {
            throw new RuntimeException("No available slots for the selected date.");
        }
    }

    public List<AppointmentDto> getAllAppointments(Long userId) {
        List<AppointmentEntity> appointments = appointmentRepository.findByPatientId(userId);
        return appointments.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private AppointmentDto convertToDto(AppointmentEntity appointmentEntity) {
        AppointmentDto appointmentDto = new AppointmentDto();
        appointmentDto.setId(appointmentEntity.getId());
        appointmentDto.setDoctorId(appointmentEntity.getDoctor().getId());
        appointmentDto.setPatientId(appointmentEntity.getPatient().getId());
        appointmentDto.setAppointmentDate(appointmentEntity.getAppointmentDate());
        appointmentDto.setSlotNumber(appointmentEntity.getSlotNumber());
        appointmentDto.setStatus(appointmentEntity.getStatus());
        appointmentDto.setReasonForVisit(appointmentEntity.getReasonForVisit());
        appointmentDto.setPayment(appointmentEntity.getPayment());
        // Fetch the doctor's name from the User repository using doctorId
        UserEntity doctor = userRepository.findById(appointmentEntity.getDoctor().getId())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
        appointmentDto.setDoctorName(doctor.getName()); // Assuming User entity has a getName() method

        return appointmentDto;
    }

}
