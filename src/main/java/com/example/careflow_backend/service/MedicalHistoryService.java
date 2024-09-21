package com.example.careflow_backend.service;

import com.example.careflow_backend.Entity.MedicalHistoryEntity;
import com.example.careflow_backend.dto.MedicalHistoryDto;
import com.example.careflow_backend.repository.MedicalHistoryRepo;
import com.example.careflow_backend.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MedicalHistoryService {

    private final MedicalHistoryRepo medicalHistoryRepo;
    private final UserRepo userRepository;

    public MedicalHistoryDto addMedicalHistory(MedicalHistoryDto medicalHistoryDto) {
        MedicalHistoryEntity entity = new MedicalHistoryEntity();
        entity.setDoctor(userRepository.findById(medicalHistoryDto.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Doctor not found")));
        entity.setPatient(userRepository.findById(medicalHistoryDto.getPatientId())
                .orElseThrow(() -> new RuntimeException("Patient not found")));
        entity.setDetails(medicalHistoryDto.getDetails());
        entity.setDate(medicalHistoryDto.getDate());

        MedicalHistoryEntity savedEntity = medicalHistoryRepo.save(entity);
        medicalHistoryDto.setId(savedEntity.getId());
        return medicalHistoryDto;
    }

    public List<MedicalHistoryDto> getMedicalHistoryByPatientId(Long patientId) {
        return medicalHistoryRepo.findByPatientId(patientId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<MedicalHistoryDto> getMedicalHistoryByDoctorId(Long doctorId) {
        return medicalHistoryRepo.findByDoctorId(doctorId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public MedicalHistoryDto updateMedicalHistory(Long id, MedicalHistoryDto medicalHistoryDto) {
        MedicalHistoryEntity entity = medicalHistoryRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Medical history not found"));

        entity.setDetails(medicalHistoryDto.getDetails());
        entity.setDate(medicalHistoryDto.getDate());


        MedicalHistoryEntity updatedEntity = medicalHistoryRepo.save(entity);
        return convertToDto(updatedEntity);
    }

    public void deleteMedicalHistory(Long id) {
        medicalHistoryRepo.deleteById(id);
    }

    private MedicalHistoryDto convertToDto(MedicalHistoryEntity entity) {
        MedicalHistoryDto dto = new MedicalHistoryDto();
        dto.setId(entity.getId());
        dto.setDoctorId(entity.getDoctor().getId());
        dto.setPatientId(entity.getPatient().getId());
        dto.setDate(entity.getDate());
        return dto;
    }
}