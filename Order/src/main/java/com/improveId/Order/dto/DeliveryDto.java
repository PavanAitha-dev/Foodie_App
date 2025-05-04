package com.improveId.Order.dto;

import com.improveId.Order.entity.DeliveryStatus;
import lombok.Data;

@Data
public class DeliveryDto {
    private Long id;
    private Long orderId;
    private Long deliveryPersonId;

    private String restaurantName;
    private String restaurantAddress;

    private String customerName;
    private String customerAddress;

    private String deliveryStatus;
}
