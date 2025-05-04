package com.improveId.Order.dto;


import lombok.Data;

@Data
public class ItemDto {
    private Long itemId;
    private String itemName;
    private Double itemPrice;
    private Long quantity;
}
