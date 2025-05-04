package com.improveid.restaurant.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ItemDto {
    private Long itemId;
    private Long restaurantId;
    private String category;
    private String name;
    private Double price;
    private String description;
    private Boolean isVegetarian;


}
