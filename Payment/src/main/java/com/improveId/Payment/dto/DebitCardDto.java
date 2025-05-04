package com.improveId.Payment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DebitCardDto extends PaymentDto {
    private String cardNumber;
    private String cvv;
    private String expiryDate;
    private String cardHolderName;
}
