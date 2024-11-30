package com.example.careflow_backend.repository;

import com.example.careflow_backend.Entity.PaymentDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentDetailsRepo extends JpaRepository<PaymentDetailsEntity, Long> {
    // You can define custom queries here if needed.
}
