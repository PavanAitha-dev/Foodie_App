package com.improveid.restaurant.exception;

public class RestaurantNotFoundException extends Exception{
    public RestaurantNotFoundException(String restaurantName) {
        super("Restaurant not Found"+restaurantName);
    }
}
