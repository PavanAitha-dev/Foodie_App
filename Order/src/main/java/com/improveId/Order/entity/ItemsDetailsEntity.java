package com.improveId.Order.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;


@Entity
@Table(name = "Items_Details")
@Data
public class ItemsDetailsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "item_id")
    private Long itemId;

    @Column(name = "item_name")
    private String itemName;

    @Column(name = "item_price")
    private Double itemPrice;

    @Column(name = "quantity")
    private Long quantity;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name = "order_id")
    @JsonBackReference
    private OrderDetailsEntity order;
}
