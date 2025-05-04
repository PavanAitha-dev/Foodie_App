package com.improveId.Payment.service;

import org.springframework.stereotype.Component;

@Component("CashonDelivery")
public final class CashOnDelivery implements PaymentStrategy {
    @Override
    public boolean pay(double amount) {
        System.out.println("Paid " + amount + " CashOnDelivery");
        return true;
    }
}
