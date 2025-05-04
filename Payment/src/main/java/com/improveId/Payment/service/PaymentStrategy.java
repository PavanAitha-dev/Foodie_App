package com.improveId.Payment.service;


public sealed interface PaymentStrategy permits CashOnDelivery,CreditCardPayment,DebitCardPayment,UpiPayment {
    boolean pay(double amount);
}

