package com.improveId.Order.feignClient;

import com.improveId.Order.dto.RestaurantDto;
import com.improveId.Order.dto.recordRestaurant;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@FeignClient(name = "restaurants-service")
public interface RestaurantClient {

    @GetMapping(path = "restaurant/byId/{id}",produces = "application/json")
    RestaurantDto getRestaurantById(@PathVariable("id") Long id);
    @GetMapping(path="restaurant/getNameAddress")
    Map<Long, recordRestaurant> getRestaurantsIdNameAddress();
}
