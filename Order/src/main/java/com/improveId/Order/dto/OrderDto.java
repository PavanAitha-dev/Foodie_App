package com.improveId.Order.dto;

import com.improveId.Order.entity.PaymentStatus;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;


@Data
public class OrderDto {
    private Long customerId;
    private Long restaurantId;
    private String paymentStatus;
    private Double totalPrice;
    private String deliveryAddress;
    private List<ItemDto> orderedItem = new ArrayList<>();

}
