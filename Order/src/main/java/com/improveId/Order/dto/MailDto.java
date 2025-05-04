package com.improveId.Order.dto;


import lombok.Data;

@Data
public class MailDto {
    private String fullName;
    private String restaurantName;
    private double amount;
    private String mailId;
    private Long orderId;



}
