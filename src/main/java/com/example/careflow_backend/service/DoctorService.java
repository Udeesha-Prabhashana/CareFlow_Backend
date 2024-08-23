package com.example.careflow_backend.service;

import com.example.careflow_backend.dto.DoctorFilterDto;
import com.example.careflow_backend.dto.UserDto;
import com.example.careflow_backend.repository.DoctorRepo;
import com.example.careflow_backend.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DoctorService {

    private final DoctorRepo doctorRepo;
    private final UserRepo userRepo;

    public List<UserDto> getFilteredDoctors(DoctorFilterDto doctorFilterDto) {
        String role = doctorFilterDto.getRoles();
        String name = doctorFilterDto.getName();
        String specialization = doctorFilterDto.getSpecialization();
        LocalDate date = doctorFilterDto.getDate();

        if (date == null && name == null) {
            return userRepo.findDoctorsByCriteria3(role, specialization);
        } else if (date == null) {
            return handleDoctorDetailsWithAvailability(role, name, specialization);
        } else {
            return userRepo.findDoctorsByCriteria(role, name, specialization, date);
        }
    }

    private List<UserDto> handleDoctorDetailsWithAvailability(String role, String name, String specialization) {
        List<Object[]> results;

        if (specialization != null) {
            results = userRepo.findDoctorDetailsWithAvailabilityAndSpecialization(role, name, specialization);
        } else {
            results = userRepo.findDoctorDetailsWithAvailability(role, name);
        }

        Map<Long, UserDto> userDtoMap = new HashMap<>();

        for (Object[] result : results) {
            Long id = (Long) result[0];
            String userName = (String) result[1];
            String emailId = (String) result[2];
            String mobileNumber = (String) result[3];
            String address = (String) result[4];
            String nameResult = (String) result[5];
            String specializationResult = (String) result[6];
            String photoUrl = (String) result[7];
            String roles = (String) result[8];
            String description = (String) result[9] ;
            LocalDate availableDate = (LocalDate) result[10];
            int bookedSlots = (int) result[11];

            UserDto userDto = userDtoMap.get(id);

            if (userDto == null) {
                userDto = new UserDto(id, userName, emailId, mobileNumber, address, nameResult, specializationResult, roles, photoUrl , description, new ArrayList<>());
                userDtoMap.put(id, userDto);
            }

            userDto.getAvailability().add(new UserDto.DoctorAvailabilityDto(availableDate, bookedSlots));
        }

        return new ArrayList<>(userDtoMap.values());
    }
}
