package com.improveId.Payment.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.improveId.Payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping("/pay/{method}")
    public ResponseEntity<?> pay(@RequestBody String Dto, @PathVariable String method) throws JsonProcessingException {
        return ResponseEntity.ok(paymentService.doPayment(method, Dto));
    }
}
