package com.example.careflow_backend.mapper;

import com.example.careflow_backend.Entity.UserEntity;
import com.example.careflow_backend.dto.UserRegistrationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserInfoMapper {

    private final PasswordEncoder passwordEncoder;
    public UserEntity convertToEntity(UserRegistrationDto userRegistrationDto) {
        UserEntity userInfoEntity = new UserEntity();
        userInfoEntity.setUserName(userRegistrationDto.userName());
        userInfoEntity.setEmailId(userRegistrationDto.userEmail());
        userInfoEntity.setMobileNumber(userRegistrationDto.userMobileNo());
        userInfoEntity.setAddress(userRegistrationDto.userAddress());
        userInfoEntity.setName(userRegistrationDto.name());
        userInfoEntity.setSpecialization(userRegistrationDto.userSpecialization());
        userInfoEntity.setRoles(userRegistrationDto.userRole());
        userInfoEntity.setPassword(passwordEncoder.encode(userRegistrationDto.userPassword()));
        return userInfoEntity;
    }
}
