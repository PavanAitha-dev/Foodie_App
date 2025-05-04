package com.improveId.Order.dto;

import com.improveId.Order.entity.PaymentStatus;
import lombok.Data;

import java.util.UUID;

@Data
public class PaymentDto {
    private UUID Id;
    private Long orderID;
    private Long customerID;
    private Long restaurantID;
    private Double Amount;
    private String typeOfPayment;
    private PaymentStatus status;

}
