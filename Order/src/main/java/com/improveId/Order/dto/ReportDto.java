package com.improveId.Order.dto;

import lombok.Data;

import java.util.List;


@Data
public class ReportDto {
    private Long totalOrders;
    private Double totalRevenue;
    List<Order> topRestaurants;

}
