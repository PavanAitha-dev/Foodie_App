package com.improveId.Payment.dto;

import com.improveId.Payment.entity.PaymentType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class PaymentDto {
    private Long orderID;
    private Long customerID;
    private Long restaurantID;
    private Double amount;
    private PaymentType paymentType;
}
