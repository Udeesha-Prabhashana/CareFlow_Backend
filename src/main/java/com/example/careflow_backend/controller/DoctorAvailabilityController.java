package com.example.careflow_backend.controller;

import com.example.careflow_backend.Entity.DoctorAvailabilityEntity;
import com.example.careflow_backend.dto.DoctorAvailabilityDto;
import com.example.careflow_backend.service.DoctorAvailabilityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api")
@CrossOrigin("http://localhost:3000")
public class DoctorAvailabilityController {

    private final DoctorAvailabilityService doctorAvailabilityService;

//    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/add_doctor_availability")
    public ResponseEntity<String> addDoctorAvailability(@Valid @RequestBody DoctorAvailabilityDto doctorAvailabilityDto) {
        try {
            // Call the service to add the appointment
            doctorAvailabilityService.createDoctorAvailability(doctorAvailabilityDto);
            return ResponseEntity.ok("Doctor availability updated successfully");
        } catch (Exception e) {
            // Log the exception and return an error response
            log.error("Error while creating appointment", e);
            return ResponseEntity.status(500).body("Failed to create appointment: " + e.getMessage());
        }
    }

//    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/get_doctor_availability")
    public ResponseEntity<?> getDoctorAvailability(@Valid @RequestBody DoctorAvailabilityDto doctorAvailabilityDto){
        try {
            // Retrieve availability information for the doctor on the specified date
            DoctorAvailabilityEntity availability = doctorAvailabilityService.getAvailabilityForDoctorOnDate(
                    doctorAvailabilityDto.getDoctorId(), doctorAvailabilityDto.getDate());

            if (availability != null) {
                // Convert the retrieved entity to a DTO
                DoctorAvailabilityDto doctorAvailabilityDto1 = new DoctorAvailabilityDto();
                doctorAvailabilityDto1.setId(availability.getId());
                doctorAvailabilityDto1.setDoctorId(availability.getDoctor().getId());
                doctorAvailabilityDto1.setDate(availability.getAvailableDate());
                doctorAvailabilityDto1.setAvailable_time(availability.getAvailableTime());
                doctorAvailabilityDto1.setTotalSlots(availability.getTotalSlots());
                doctorAvailabilityDto1.setBookedSlots(availability.getBookedSlots());

                // Return the populated DTO
                return ResponseEntity.ok(doctorAvailabilityDto1);
            } else {
                // Return a NOT FOUND response if the availability is not found
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Doctor is not available on " + doctorAvailabilityDto.getDate());
            }
        } catch (Exception e) {
            // Log the exception and return an INTERNAL SERVER ERROR response
            log.error("Error while retrieving doctor availability", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to retrieve doctor availability: " + e.getMessage());
        }
    }
}
