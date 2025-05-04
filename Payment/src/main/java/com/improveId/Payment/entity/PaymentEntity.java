package com.improveId.Payment.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.UUID;
@Data
@Entity
public class PaymentEntity {
    @Id
    @GeneratedValue
    private UUID Id;
    private Long orderID;
    private Long customerID;
    private Long restaurantID;
    private Double Amount;
    private String typeOfPayment;
    private PaymentStatus status;

}
