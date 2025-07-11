package com.improveId.Payment.service;

public class PaymentContext {
    private PaymentStrategy strategy;
    public PaymentContext(PaymentStrategy strategy) {
        this.strategy = strategy;
    }
    public PaymentStrategy setStrategy(PaymentStrategy strategy) {
        this.strategy = strategy;
        return strategy;
    }
    public String  performPaymentOperation(Double amount) {
        try{
            if(strategy.pay(amount))
                return "Payment success";
            else
                return "Payment fail";
        } catch (Exception e) {
            throw new RuntimeException("Payment failed " + e.getMessage());
        }
    }
}
