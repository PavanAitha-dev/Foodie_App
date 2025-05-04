package com.improveid.restaurant.exception;

public class ItemNotFoundException extends Exception{
    public ItemNotFoundException(Long item){
        super("Item not found"+item);

    }
}
