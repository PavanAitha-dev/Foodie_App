package com.improveId.Payment.service;

import org.springframework.stereotype.Component;

@Component("CreditCard")
public final class CreditCardPayment implements PaymentStrategy {
    @Override
    public boolean pay(double amount) {
        System.out.println("Paid " + amount + " using Credit Card.");
        return true;
    }
}