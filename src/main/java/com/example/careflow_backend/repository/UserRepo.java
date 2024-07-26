package com.example.careflow_backend.repository;

import aj.org.objectweb.asm.commons.Remapper;
import com.example.careflow_backend.Entity.UserEntity;
import com.example.careflow_backend.dto.UserDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepo extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByUserName(String emailId);

    Optional<UserEntity> findByEmailId(String emailID);

    Optional<UserEntity> findByMobileNumber(String mobileNumber);

    @Query("SELECT new com.example.careflow_backend.dto.UserDto(u.id, u.userName, u.emailId, u.mobileNumber, u.address, u.name, u.specialization, u.roles) FROM UserEntity u WHERE u.roles = :role")
    List<UserDto> findByRoles(String role);
}
