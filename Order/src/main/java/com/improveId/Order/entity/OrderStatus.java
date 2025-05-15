package com.improveId.Order.entity;

public enum OrderStatus {
    PLACED,                         //Customer placed the order, waiting for restaurant to accept.
    PREPARING,                      //Restaurant has accepted and is preparing the food.
    READY_FOR_DELIVERY,             //Food is packed and ready for pickup.
    OUT_FOR_DELIVERY,               //The partner is on the way to the customer's location.
    CANCELLED_BY_RESTAURANT,        //Restaurant canceled the order
    CANCELLED_BY_CUSTOMER,          //Customer canceled the order
    DELIVERED                       //Food successfully delivered to the customer

}