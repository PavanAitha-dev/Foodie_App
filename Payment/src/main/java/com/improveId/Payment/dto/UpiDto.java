package com.improveId.Payment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UpiDto extends PaymentDto{
    private String upiId;
}
