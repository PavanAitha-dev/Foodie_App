package com.improveId.Order.feignClient;

import com.improveId.Order.dto.MailDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name = "email-service")
public interface EmailClient {
    @RequestMapping(path = "mail/send",produces = "application/json")
    public ResponseEntity<String> sendEmail(@RequestBody MailDto request);
}
