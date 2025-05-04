package com.improveId.Order.entity;

public enum DeliveryStatus {
    ASSIGNED,                 //A delivery partner has accepted the order.
    PICKED_UP,                 //The partner has picked up the food from the restaurant.
    OUT_FOR_DELIVERY,          //The partner is on the way to the customer's location.
    DELIVERED                  //The delivery is completed and the customer has received the order.
}
