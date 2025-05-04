package com.improveId.Order.dto;


import lombok.Data;

@Data
public class ItemDetailsDto {
    private Long id;
    private String name;
    private Double price;
    private Long quantity;

}

