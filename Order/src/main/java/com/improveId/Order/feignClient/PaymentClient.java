package com.improveId.Order.feignClient;

import com.improveId.Order.dto.PaymentDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "payment-service")
public interface PaymentClient {
    @PostMapping(path = "payment/pay/{method}",produces = "application/json")
    PaymentDto pay(@RequestBody String Dto, @PathVariable("method") String method);



}
