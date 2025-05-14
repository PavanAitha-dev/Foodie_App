package com.improveId.Order.dto;

import com.improveId.Order.entity.OrderStatus;
import com.improveId.Order.entity.PaymentStatus;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class OrderDetailsDto {
    private Long id;
    private String customerName;
    private String restaurantName;
    private Double totalPrice;
    private PaymentStatus paymentStatus;
    private OrderStatus orderStatus;
    private String deliveryPersonName;
    private String deliveryAddress;
    private Long rating;
    private List<ItemDetailsDto> orderedItems = new ArrayList<>();
}
