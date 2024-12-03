package com.example.careflow_backend.mapper;

import com.example.careflow_backend.Entity.DoctorDetailsEntity;
import com.example.careflow_backend.Entity.NurseDetailsEntity;
import com.example.careflow_backend.Entity.ReceptionistDetailsEntity;
import com.example.careflow_backend.Entity.UserEntity;
import com.example.careflow_backend.dto.UserRegistrationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EntityMapper {

    private final PasswordEncoder passwordEncoder;
    public UserEntity convertToUserEntity(UserRegistrationDto userRegistrationDto) {
        UserEntity userInfoEntity = new UserEntity();

        if (userRegistrationDto.userEmail() == null || userRegistrationDto.userPassword() == null || userRegistrationDto.userRole() == null) {
            throw new IllegalArgumentException("Required fields cannot be null");
        }
        userInfoEntity.setUserName(userRegistrationDto.userName());
        userInfoEntity.setEmailId(userRegistrationDto.userEmail());
        userInfoEntity.setMobileNumber(userRegistrationDto.userMobileNo());
        userInfoEntity.setAddress(userRegistrationDto.userAddress());
        userInfoEntity.setName(userRegistrationDto.name());
        userInfoEntity.setRoles(userRegistrationDto.userRole());
        userInfoEntity.setPassword(passwordEncoder.encode(userRegistrationDto.userPassword()));

        // Set missing fields
        userInfoEntity.setSpecialization(userRegistrationDto.userSpecialization());
        userInfoEntity.setPhotoUrl(userRegistrationDto.photoUrl());
        userInfoEntity.setDescription(userRegistrationDto.description());

        return userInfoEntity;
    }


    public DoctorDetailsEntity convertDoctorDetailsEntity(UserEntity userEntity , UserRegistrationDto userRegistrationDto){
        DoctorDetailsEntity doctorDetailsEntity = new DoctorDetailsEntity();
        doctorDetailsEntity.setDoctor(userEntity);
        doctorDetailsEntity.setBookingCharge(userRegistrationDto.BookingCharge());
        doctorDetailsEntity.setRegistrationNumber(userRegistrationDto.registrationNumber());
        doctorDetailsEntity.setSpecialization(userRegistrationDto.userSpecialization());
        doctorDetailsEntity.setDescription(userRegistrationDto.description());
        return doctorDetailsEntity;
    }

    public NurseDetailsEntity convertNurseDetailsEntity(UserEntity userEntity, UserRegistrationDto userRegistrationDto) {
        NurseDetailsEntity nurseDetailsEntity = new NurseDetailsEntity();
        nurseDetailsEntity.setNurse(userEntity);
        nurseDetailsEntity.setRegistrationNumber(userRegistrationDto.registrationNumber());
        nurseDetailsEntity.setSpecialization(userRegistrationDto.userSpecialization());
        nurseDetailsEntity.setDepartment(userRegistrationDto.department());
        nurseDetailsEntity.setExperienceYears(userRegistrationDto.experienceYears());
        nurseDetailsEntity.setDescription(userRegistrationDto.description());
        return nurseDetailsEntity;
    }
    public ReceptionistDetailsEntity convertReceptionistDetailsEntity(UserEntity userEntity, UserRegistrationDto userRegistrationDto) {
        ReceptionistDetailsEntity receptionistDetailsEntity = new ReceptionistDetailsEntity();
        receptionistDetailsEntity.setReceptionist(userEntity);
        receptionistDetailsEntity.setShift(userRegistrationDto.shift());
        receptionistDetailsEntity.setDepartment(userRegistrationDto.department());
        receptionistDetailsEntity.setExperienceYears(userRegistrationDto.experienceYears());
        receptionistDetailsEntity.setDescription(userRegistrationDto.description());
        return receptionistDetailsEntity;
    }


}
