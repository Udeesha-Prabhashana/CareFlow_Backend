package com.example.careflow_backend.service;

import com.example.careflow_backend.Entity.*;
import com.example.careflow_backend.dto.AppointmentDetailsDto;
import com.example.careflow_backend.dto.AppointmentDto;
import com.example.careflow_backend.dto.PaymentDetailsDto;
import com.example.careflow_backend.repository.AppointmentRepo;
import com.example.careflow_backend.repository.DoctorDetailsRepo;
import com.example.careflow_backend.repository.PaymentDetailsRepo;
import com.example.careflow_backend.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class AppointmentService {
    private final AppointmentRepo appointmentRepository;
    private final UserRepo userRepository;
    private final DoctorAvailabilityService doctorAvailabilityService;
    private final PaymentDetailsRepo paymentDetailsRepository;
    private final DoctorDetailsRepo doctorDetailsRepo;
    private final NotificationService notificationService;

    public AppointmentDto addAppointment(AppointmentDto appointmentDto, Long userId) {
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
        appointmentEntity.setIsCancelled(false);

        // Retrieve Doctor Availability for the given date
        DoctorAvailabilityEntity availability = doctorAvailabilityService.getAvailabilityForDoctorOnDate(
                appointmentDto.getDoctorId(), appointmentDto.getAppointmentDate());

        System.out.println("Availability" + availability);

        // Check if slots are available
        if (availability != null && availability.getBookedSlots() < availability.getTotalSlots() &&
                availability.getBookedSlots() + 1 == appointmentDto.getSlotNumber()) {

            // Increment the booked slots and save the availability
            availability.setBookedSlots(availability.getBookedSlots() + 1);
            doctorAvailabilityService.saveAvailability(availability);

            // Save the appointment entity to the database
            AppointmentEntity savedAppointment = appointmentRepository.save(appointmentEntity);

            // Send confirmation message to the user
            try {
                String userPhoneNumber = appointmentEntity.getPatient().getMobileNumber();
                if (userPhoneNumber == null || userPhoneNumber.isEmpty()) {
                    throw new RuntimeException("User phone number is missing.");
                }
                String formattedPhoneNumber = notificationService.formatPhoneNumber(userPhoneNumber);

                String message = String.format("Dear %s, your appointment with Dr. %s is confirmed for %s at slot %d. Reason: %s. Thank you!",
                        appointmentEntity.getPatient().getName(),
                        appointmentEntity.getDoctor().getName(),
                        appointmentEntity.getAppointmentDate().toString(),
                        appointmentEntity.getSlotNumber(),
                        appointmentEntity.getReasonForVisit());

                notificationService.sendMessage(formattedPhoneNumber, message);
                log.info("Appointment confirmation message sent to user: {}", formattedPhoneNumber);
            } catch (Exception e) {
                log.error("Error while sending appointment confirmation message: {}", e.getMessage());
                throw new RuntimeException("Failed to send appointment confirmation message.", e);
            }

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
                .filter(appointment -> appointment.getIsCancelled() == null || !appointment.getIsCancelled()) // Handle nulls gracefully
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<AppointmentDto> getAllAppointmentsByAdmin() {
        List<AppointmentEntity> appointments = appointmentRepository.findAll();
        return appointments.stream()
                .map(appointment -> {
                    AppointmentDto appointmentDto = convertToDto(appointment);
                    // Extract patient name and set it in the AppointmentDto
                    String patientName = appointment.getPatient() != null ? appointment.getPatient().getName() : null;
                    appointmentDto.setPatientName(patientName);
                    return appointmentDto;
                })
                .collect(Collectors.toList());
    }

    public List<AppointmentDto> getAllAppointmentsDoctor(Long userId) {
        List<AppointmentEntity> appointments = appointmentRepository.findByDoctorId(userId);
        return appointments.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public int getAppointmentCount(Long doctorId, String type) {
        switch (type) {
            case "ong_appointments":
                return appointmentRepository.countByDoctorIdAndStatus(doctorId, 1); // Ongoing
            case "upcom_appointments":
                return appointmentRepository.countByDoctorIdAndStatus(doctorId, 0); // Upcoming
            case "miss_appointments":
                return appointmentRepository.countByDoctorIdAndStatus(doctorId, -1); // Missed
            default:
                throw new IllegalArgumentException("Invalid appointment type: " + type);
        }
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

    public List<AppointmentDto> getHistory(Long doctorId) {
        return appointmentRepository.findHistory(doctorId)
                .stream()
                .map(a -> new AppointmentDto(a.getId(), a.getPatient().getName(), a.getAppointmentDate(), a.getSlotNumber()))
                .collect(Collectors.toList());
    }

    public List<AppointmentDto> getToday(Long doctorId) {
        return appointmentRepository.findToday(doctorId)
                .stream()
                .map(a -> new AppointmentDto(a.getId(), a.getPatient().getName(), a.getAppointmentDate(), a.getSlotNumber()))
                .collect(Collectors.toList());
    }

    public List<AppointmentDto> getUpcoming(Long doctorId) {
        return appointmentRepository.findUpcoming(doctorId)
                .stream()
                .map(a -> new AppointmentDto(a.getId(), a.getPatient().getName(), a.getAppointmentDate(), a.getSlotNumber()))
                .collect(Collectors.toList());
    }

    public AppointmentDto addAppointmentWithPay(AppointmentDto appointmentDto, Long userId) {
        // Create the AppointmentEntity
        AppointmentEntity appointment = new AppointmentEntity();
        System.out.println("Iddddddddd" + appointmentDto.getDoctorId());
        appointment.setDoctor(userRepository.findById(appointmentDto.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Doctor not found")));
        appointment.setPatient(userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Patient not found")));
        appointment.setAppointmentDate(appointmentDto.getAppointmentDate());
        appointment.setSlotNumber(appointmentDto.getSlotNumber());
        appointment.setStatus(appointmentDto.getStatus());
        appointment.setReasonForVisit(appointmentDto.getReasonForVisit());
        appointment.setPayment(appointmentDto.getPayment());
        appointment.setIsCancelled(false);

        // Retrieve Doctor Availability for the given date
        DoctorAvailabilityEntity availability = doctorAvailabilityService.getAvailabilityForDoctorOnDate(
                appointmentDto.getDoctorId(), appointmentDto.getAppointmentDate());

        if (availability != null && availability.getBookedSlots() < availability.getTotalSlots()
                && availability.getBookedSlots() + 1 == appointmentDto.getSlotNumber()) {
            // Increment the booked slots and save the availability
            availability.setBookedSlots(availability.getBookedSlots() + 1);
            doctorAvailabilityService.saveAvailability(availability);

            // Save the appointment entity
            AppointmentEntity savedAppointment = appointmentRepository.save(appointment);

            // Process Payment Details
            PaymentDetailsDto paymentDetailsDto = appointmentDto.getPaymentDetails();
            if (paymentDetailsDto == null) {
                throw new RuntimeException("Payment details are required.");
            }

            PaymentDetailsEntity paymentDetails = new PaymentDetailsEntity();
            paymentDetails.setAppointment(savedAppointment);
            paymentDetails.setDoctor(savedAppointment.getDoctor());
            paymentDetails.setPatient(savedAppointment.getPatient());
            paymentDetails.setPaymentDate(LocalDateTime.now());
            paymentDetails.setAmountPaid(paymentDetailsDto.getAmountPaid());

            paymentDetailsRepository.save(paymentDetails);

            // Send confirmation message to the user
            try {
                String userPhoneNumber = savedAppointment.getPatient().getMobileNumber();
                if (userPhoneNumber == null || userPhoneNumber.isEmpty()) {
                    throw new RuntimeException("User phone number is missing.");
                }
                String formattedPhoneNumber = notificationService.formatPhoneNumber(userPhoneNumber);

                String message = String.format(
                        "Dear %s, your appointment with Dr. %s is confirmed for %s at slot %d. " +
                                "Payment of Rs. %.2f has been successfully received. Thank you!",
                        savedAppointment.getPatient().getName(),
                        savedAppointment.getDoctor().getName(),
                        savedAppointment.getAppointmentDate().toString(),
                        savedAppointment.getSlotNumber(),
                        paymentDetails.getAmountPaid().doubleValue() // Cast to double
                );


                notificationService.sendMessage(formattedPhoneNumber, message);
                log.info("Appointment confirmation message sent to user: {}", formattedPhoneNumber);
            } catch (Exception e) {
                log.error("Error while sending appointment confirmation message: {}", e.getMessage());
                throw new RuntimeException("Failed to send appointment confirmation message.", e);
            }

            // Convert the saved appointment to DTO and return
            appointmentDto.setId(savedAppointment.getId());
            appointmentDto.setPaymentDetails(paymentDetailsDto);
            return appointmentDto;
        } else {
            throw new RuntimeException("No available slots for the selected date.");
        }
    }

    public void cancelAppointment(Long appointmentId) {
        AppointmentEntity appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        if (appointment.getIsCancelled()) { // Updated
            throw new RuntimeException("Appointment is already canceled");
        }

        // Update the slot availability
        DoctorAvailabilityEntity availability = doctorAvailabilityService.getAvailabilityForDoctorOnDate(
                appointment.getDoctor().getId(), appointment.getAppointmentDate());

        if (availability != null) {
            availability.setBookedSlots(availability.getBookedSlots() - 1);
            doctorAvailabilityService.saveAvailability(availability);
        }

        // Mark the appointment as canceled
        appointment.setIsCancelled(true);
        appointmentRepository.save(appointment);

        try {
            String userPhoneNumber = appointment.getPatient().getMobileNumber();
            String formattedPhoneNumber = notificationService.formatPhoneNumber(userPhoneNumber);

            String message = String.format("Dear %s, your appointment with Dr. %s on %s has been canceled.",
                    appointment.getPatient().getName(),
                    appointment.getDoctor().getName(),
                    appointment.getAppointmentDate().toString());

            notificationService.sendMessage(formattedPhoneNumber, message);
            log.info("Cancellation message sent to user: {}", formattedPhoneNumber);
        } catch (Exception e) {
            log.error("Error while sending cancellation message: {}", e.getMessage());
        }
    }


    public AppointmentDetailsDto getAppointmentDetails(Long appointmentId) {
        // Fetch appointment details
        AppointmentEntity appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        // Map details to DTO
        AppointmentDetailsDto dto = new AppointmentDetailsDto();
        dto.setAppointmentId(appointment.getId());
        dto.setAppointmentDate(appointment.getAppointmentDate());
        dto.setSlotNumber(appointment.getSlotNumber());
        dto.setReasonForVisit(appointment.getReasonForVisit());
        dto.setDoctorId(appointment.getDoctor().getId());

        DoctorDetailsEntity doctorDetailsEntity = doctorDetailsRepo.findByDoctorId(appointment.getDoctor().getId());
        if (doctorDetailsEntity == null) {
            throw new RuntimeException("Doctor details not found for the given doctor ID");
        }

        dto.setPayment(doctorDetailsEntity.getBookingCharge());

        return dto;
    }


}
