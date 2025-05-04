package com.improveId.Order;

import com.improveId.Order.dto.ItemDetailsDto;
import com.improveId.Order.dto.OrderDetailsDto;
import com.improveId.Order.entity.ItemsDetailsEntity;
import com.improveId.Order.entity.OrderDetailsEntity;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SpringBootApplication
@EnableFeignClients
public class OrderApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderApplication.class, args);
	}

}
