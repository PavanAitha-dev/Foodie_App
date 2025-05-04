package com.improveId.Payment.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.improveId.Payment.dto.*;
import com.improveId.Payment.entity.PaymentEntity;
import com.improveId.Payment.entity.PaymentStatus;
import com.improveId.Payment.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    @Autowired
    ObjectMapper objectMapper;

    private final Map<String, PaymentStrategy> paymentStrategies;

    @Autowired
    public PaymentService(List<PaymentStrategy> strategyList, PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
        paymentStrategies = new HashMap<>();
        for (PaymentStrategy strategy : strategyList) {
            String beanName = strategy.getClass().getAnnotation(Component.class).value();
            //cashOnDelivery
            paymentStrategies.put(beanName, strategy);
        }
    }


    public PaymentEntity doPayment(String method, String request) throws JsonProcessingException {
        if (method.equals("CreditCard")) {
            CreditCardDto codDto = objectMapper.readValue(request, CreditCardDto.class);
           return processPayment(method,codDto);
        }
        else if (method.equals("DebitCard")) {
            DebitCardDto debitCardDto = objectMapper.readValue(request, DebitCardDto.class);
           return processPayment(method,debitCardDto);
        }
        else if (method.equals("UPI")) {
            UpiDto debitCardDto = objectMapper.readValue(request, UpiDto.class);
           return processPayment(method,debitCardDto);
        }
        else if (method.equals("CashonDelivery")) {
            CashOnDeliveryDto debitCardDto = objectMapper.readValue(request, CashOnDeliveryDto.class);
            return processPayment(method,debitCardDto);
        }
        else {
            return null;
        }
    }
    public PaymentEntity processPayment(String method, PaymentDto dto) {
        PaymentStrategy strategy = paymentStrategies.get(method);
        if (strategy == null) {
            throw new IllegalArgumentException("Unsupported payment method: " + method);
        }
        PaymentEntity result=null;
        if(strategy.pay(dto.getAmount())){
            PaymentEntity payment = new PaymentEntity();
            payment.setAmount(dto.getAmount());
            payment.setStatus(PaymentStatus.SUCCESS);
            payment.setRestaurantID(dto.getRestaurantID());
            payment.setOrderID(dto.getOrderID());
            payment.setCustomerID(dto.getCustomerID());
            payment.setTypeOfPayment(method);
            result= paymentRepository.save(payment);
            if(result.getStatus()==PaymentStatus.SUCCESS){

            }
        }
        return result;
    }
}
