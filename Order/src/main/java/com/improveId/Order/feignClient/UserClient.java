package com.improveId.Order.feignClient;


import com.improveId.Order.dto.UserProfileDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@FeignClient(name = "user-service")
public interface UserClient {
    @GetMapping(path = "user/findById/{id}",produces = "application/json")
    UserProfileDto getById(@PathVariable("id") Long id);

    @GetMapping(path = "user/idName",produces = "application/json")
    Map<Long,String> getIdName();
}
