package com.improveId.Payment.service;

import org.springframework.stereotype.Component;

@Component("DebitCard")
public final class DebitCardPayment implements PaymentStrategy {
    @Override
    public boolean pay(double amount) {
        System.out.println("Paid " + amount + " using DebitCardPayment.");
        return true;
    }
}

