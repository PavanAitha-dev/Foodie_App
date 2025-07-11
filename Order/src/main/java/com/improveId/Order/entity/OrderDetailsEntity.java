package com.improveId.Order.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
    @Table(name = "Order_Details")
    @Data
    @RequiredArgsConstructor
    public class OrderDetailsEntity {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id")
        private Long id;

        @Column(name = "customer_id")
        private Long customerId;

        @Column(name = "restaurant_id")
        private Long restaurantId;

        @Column(name = "total_price")
        private Double totalPrice;

        @Enumerated(EnumType.STRING)
        @Column(name = "payment_status")
        private PaymentStatus paymentStatus;

        @Column(name = "delivery_person_id")
        private Long deliveryPersonId;

        @OneToMany(mappedBy = "order", cascade = CascadeType.PERSIST)
        @JsonManagedReference
        private List<ItemsDetailsEntity> orderedItems = new ArrayList<>();

        @Column(name = "order_status")
        @Enumerated(EnumType.STRING)
        private OrderStatus orderStatus;

        private String deliveryAddress;

        private Long rating;

        private LocalDateTime orderTimestamp;

}
