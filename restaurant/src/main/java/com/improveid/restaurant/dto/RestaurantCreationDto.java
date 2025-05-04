package com.improveid.restaurant.dto;

import lombok.Data;

@Data
public class RestaurantCreationDto {
    private String name;
    private String address;
    private String contactNumber;
    private String openingHours;
    private String email;
}
