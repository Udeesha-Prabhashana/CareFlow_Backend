package com.example.careflow_backend.service;

import com.example.careflow_backend.dto.PaymentDetailsDto;
import com.example.careflow_backend.Entity.AppointmentEntity;
import com.example.careflow_backend.Entity.PaymentDetailsEntity;
import com.example.careflow_backend.Entity.UserEntity;
import com.example.careflow_backend.repository.AppointmentRepo;
import com.example.careflow_backend.repository.PaymentDetailsRepo;

import com.example.careflow_backend.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class PaymentService {

    @Autowired
    private PaymentDetailsRepo paymentDetailsRepository;

    @Autowired
    private AppointmentRepo appointmentRepository;

    @Autowired
    private UserRepo userRepository;

    @Transactional
    public void addPayment(PaymentDetailsDto paymentDetailsDto , Long userId) {
        // Retrieve related entities
        AppointmentEntity appointment = appointmentRepository.findById(paymentDetailsDto.getAppointmentId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid appointment ID"));

        UserEntity doctor = userRepository.findById(paymentDetailsDto.getDoctorId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid doctor ID"));

        UserEntity patient = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid patient ID"));

        // Create a new payment record
        PaymentDetailsEntity paymentDetailsEntity = new PaymentDetailsEntity();
        paymentDetailsEntity.setAppointment(appointment);
        paymentDetailsEntity.setDoctor(doctor);
        paymentDetailsEntity.setPatient(patient);
        paymentDetailsEntity.setPaymentDate(LocalDateTime.now()); // Use current date-time for payment
        paymentDetailsEntity.setAmountPaid(paymentDetailsDto.getAmountPaid());

        // Save the payment record
        paymentDetailsRepository.save(paymentDetailsEntity);

        // Update the payment status in the appointment
        appointment.setPayment(1); // Mark as paid
        appointmentRepository.save(appointment);
    }
}
