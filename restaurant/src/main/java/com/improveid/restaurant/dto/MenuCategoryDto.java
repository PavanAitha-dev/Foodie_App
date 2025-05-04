package com.improveid.restaurant.dto;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class MenuCategoryDto {
    private String name;
    private Long restaurantId;
}
