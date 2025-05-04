package com.improveId.Order.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="delivery_Details")
@Data
public class DeliveryDetailsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long deliveryPersonId;

    private Long orderId;
    private String restaurantName;

    private String customerName;

    private String pickupLocation;
    private String deliveryLocation;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;


}
