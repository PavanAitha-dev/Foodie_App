package com.improveId.Payment.repository;

import com.improveId.Payment.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PaymentRepository extends JpaRepository<PaymentEntity,Long> {
}
