package com.improveId.Order.exception;

public class NotFoundException extends Exception{
    public NotFoundException(String message){
        super("Not found Exception"+message);

    }
}
