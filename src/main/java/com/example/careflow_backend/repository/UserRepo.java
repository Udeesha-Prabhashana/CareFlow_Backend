package com.example.careflow_backend.repository;

import aj.org.objectweb.asm.commons.Remapper;
import com.example.careflow_backend.Entity.UserEntity;
import com.example.careflow_backend.dto.UserDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserRepo extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByUserName(String emailId);

    Optional<UserEntity> findByEmailId(String emailID);

    Optional<UserEntity> findByEmailIdAndIsVerified(String emailId, boolean isVerified);

    Optional<UserEntity> findByMobileNumber(String mobileNumber);
    Optional<UserEntity> findByMobileNumberAndOtp(String mobileNumber, String otp);

    @Query("SELECT new com.example.careflow_backend.dto.UserDto(u.id, u.userName, u.emailId, u.mobileNumber, u.address, u.name, dd.specialization, u.roles, u.photoUrl, dd.description, dd.BookingCharge, dd.registrationNumber) " +
            "FROM UserEntity u " +
            "JOIN u.doctorDetails dd " +
            "WHERE u.roles = :role")
    List<UserDto> findDoctorsByRole(String role);

    @Query("SELECT new com.example.careflow_backend.dto.UserDto(u.id, u.userName, u.emailId, u.mobileNumber, u.address, u.name, u.roles, u.photoUrl) " +
            "FROM UserEntity u WHERE u.roles = :role")
    List<UserDto> findUsersByRole(String role);




    @Query("SELECT new com.example.careflow_backend.dto.UserDto(u.id, u.userName, u.emailId, u.mobileNumber, u.address, u.name, dd.specialization, u.roles, u.photoUrl, da.availableDate, da.availableTime, da.totalSlots, da.bookedSlots , dd.description , dd.BookingCharge) " +
            "FROM UserEntity u " +
            "JOIN u.doctorDetails dd " +
            "JOIN u.availability da " +
            "WHERE u.roles = :role " +
            "AND LOWER(u.name) LIKE LOWER(CONCAT('%', :name, '%')) " +
            "AND LOWER(dd.specialization) LIKE LOWER(CONCAT('%', :specialization, '%')) " +
            "AND da.availableDate = :availableDate")
    List<UserDto> findDoctorsByCriteria(
            String role,
            String name,
            String specialization,
            LocalDate availableDate
    );


    @Query("SELECT new com.example.careflow_backend.dto.UserDto(u.id, u.userName, u.emailId, u.mobileNumber, u.address, u.name, dd.specialization, u.photoUrl, u.roles, da.availableDate, da.availableTime, da.totalSlots, da.bookedSlots , dd.description ,dd.BookingCharge) " +
            "FROM UserEntity u " +
            "JOIN u.doctorDetails dd " +
            "JOIN u.availability da " +
            "WHERE u.roles = :role " +
            "AND LOWER(u.name) LIKE LOWER(CONCAT('%', :name, '%')) " +
            "AND LOWER(dd.specialization) LIKE LOWER(CONCAT('%', :specialization, '%')) ")
    List<UserDto> findDoctorsByCriteria2(
            String role,
            String name,
            String specialization
    );


    @Query("SELECT new com.example.careflow_backend.dto.UserDto(u.id, u.userName, u.emailId, u.mobileNumber, u.address, u.name, dd.specialization, u.photoUrl, u.roles, da.availableDate, da.availableTime, da.totalSlots, da.bookedSlots, dd.description ,dd.BookingCharge) " +
            "FROM UserEntity u " +
            "JOIN u.doctorDetails dd " +
            "JOIN u.availability da " +
            "WHERE u.roles = :role " +
            "AND LOWER(dd.specialization) LIKE LOWER(CONCAT('%', :specialization, '%')) ")
    List<UserDto> findDoctorsByCriteria3(
            String role,
            String specialization
    );

    @Query("SELECT new com.example.careflow_backend.dto.UserDto(u.id, u.userName, u.emailId, u.mobileNumber, u.address, u.name, dd.specialization, u.photoUrl, u.roles, da.availableDate, da.availableTime, da.totalSlots, da.bookedSlots, dd.description ,dd.BookingCharge) " +
            "FROM UserEntity u " +
            "JOIN u.doctorDetails dd " +
            "JOIN u.availability da " +
            "WHERE u.roles = :role " +
            "AND LOWER(u.name) LIKE LOWER(CONCAT('%', :name, '%')) ")
    List<UserDto> findDoctorsByCriteria4(
            String role,
            String name
    );

    @Query("SELECT u.id, u.userName, u.emailId, u.mobileNumber, u.address, u.name, dd.specialization, u.photoUrl, u.roles, dd.description, da.availableDate,da.availableTime, da.totalSlots, da.bookedSlots ,dd.BookingCharge " +
            "FROM UserEntity u " +
            "JOIN u.doctorDetails dd " +
            "JOIN u.availability da " +
            "WHERE u.roles = :role " +
            "AND LOWER(u.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Object[]> findDoctorDetailsWithAvailability(String role, String name);


    @Query("SELECT u.id, u.userName, u.emailId, u.mobileNumber, u.address, u.name, dd.specialization, u.photoUrl, u.roles, dd.description, da.availableDate,da.availableTime, da.totalSlots, da.bookedSlots, dd.BookingCharge " +
            "FROM UserEntity u " +
            "JOIN u.doctorDetails dd " +
            "JOIN u.availability da " +
            "WHERE u.roles = :role " +
            "AND LOWER(u.name) LIKE LOWER(CONCAT('%', :name, '%')) " +
            "AND LOWER(dd.specialization) LIKE LOWER(CONCAT('%', :specialization, '%'))")
    List<Object[]> findDoctorDetailsWithAvailabilityAndSpecialization(String role, String name, String specialization);



}
