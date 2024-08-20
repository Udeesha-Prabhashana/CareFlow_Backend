package com.example.careflow_backend.service;

import com.example.careflow_backend.Entity.DoctorAvailabilityEntity;
import com.example.careflow_backend.dto.DoctorAvailabilityDto;
import com.example.careflow_backend.repository.DoctorAvailabilityRepo;
import com.example.careflow_backend.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DoctorAvailabilityService {

    private final DoctorAvailabilityRepo doctorAvailabilityRepo;
    private final UserRepo userRepository;
    public DoctorAvailabilityDto createDoctorAvailability( DoctorAvailabilityDto doctorAvailabilityDto) {
        DoctorAvailabilityEntity availabilityEntity = new DoctorAvailabilityEntity();
        availabilityEntity.setDoctor(userRepository.findById(doctorAvailabilityDto.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Doctor not found")));
        availabilityEntity.setAvailableDate(doctorAvailabilityDto.getDate());
        availabilityEntity.setTotalSlots(doctorAvailabilityDto.getTotalSlots());
        availabilityEntity.setBookedSlots(doctorAvailabilityDto.getBookedSlots());
        availabilityEntity.setAvailableTime(doctorAvailabilityDto.getAvailable_time());

        DoctorAvailabilityEntity savedAvailabilityEntity = doctorAvailabilityRepo.save(availabilityEntity);

        doctorAvailabilityDto.setId((savedAvailabilityEntity.getId()));
        return doctorAvailabilityDto;
    }

    public DoctorAvailabilityEntity getAvailabilityForDoctorOnDate(Long doctorId, LocalDate date) {
        System.out.println("Date"+ date);
        return doctorAvailabilityRepo.findByDoctor_IdAndAvailableDate(doctorId, date)
                .orElse(null); // Return null if not found
    }

    public DoctorAvailabilityEntity saveAvailability(DoctorAvailabilityEntity availability) {
        return doctorAvailabilityRepo.save(availability);
    }
    public List<DoctorAvailabilityDto> getAllAvailability() {
        return doctorAvailabilityRepo.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<DoctorAvailabilityDto> getAvailabilityByDoctorId(Long doctorId) {
        return doctorAvailabilityRepo.findByDoctorId(doctorId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public DoctorAvailabilityDto getAvailabilityById(Long id) {
        return doctorAvailabilityRepo.findById(id)
                .map(this::convertToDto)
                .orElse(null);
    }

//    public List<DoctorAvailabilityDto> getAvailabilityByDoctorIdAndDate(Long doctorId, LocalDate date) {
//        return doctorAvailabilityRepo.findByDoctorIdAndAvailableDate(doctorId, date).stream()
//                .map(this::convertToDto)
//                .collect(Collectors.toList());
//    }

    public void deleteAvailability(Long id) {
        doctorAvailabilityRepo.deleteById(id);
    }

    // Helper methods to convert between entity and DTO

    private DoctorAvailabilityDto convertToDto(DoctorAvailabilityEntity availabilityEntity) {
        return new DoctorAvailabilityDto(
                availabilityEntity.getId(),
                availabilityEntity.getDoctor() != null ? availabilityEntity.getDoctor().getId() : null, // Extract doctorId
                availabilityEntity.getAvailableDate(),
                availabilityEntity.getTotalSlots(),
                availabilityEntity.getAvailableTime(),
                availabilityEntity.getBookedSlots()
        );
    }

    private DoctorAvailabilityEntity convertToEntity(DoctorAvailabilityEntity doctorAvailabilityEntity) {
        return new DoctorAvailabilityEntity(
                doctorAvailabilityEntity.getId(),
                doctorAvailabilityEntity.getDoctor(),
                doctorAvailabilityEntity.getAvailableDate(),
                doctorAvailabilityEntity.getAvailableTime(),
                doctorAvailabilityEntity.getTotalSlots(),
                doctorAvailabilityEntity.getBookedSlots()
        );
    }
}