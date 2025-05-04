package com.improveId.Order.dto;

import lombok.Data;

@Data
public class RestaurantDto {
    private Long id;
    private String name;
    private String contactNumber;
    private String email;
    private String address;
    private Double Rating;
    private String openingHours;
    private String status;

}
