package com.improveId.Payment.service;

import org.springframework.stereotype.Component;

@Component("UPI")
public final class UpiPayment implements PaymentStrategy {
    @Override
    public boolean pay(double amount) {
        System.out.println("Paid " + amount + " using UPI.");
        return  true;
    }
}
