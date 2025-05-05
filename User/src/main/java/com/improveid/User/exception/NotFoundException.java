package com.improveid.User.exception;

public class NotFoundException extends Exception{
    public NotFoundException(String message){
        super("Not found Exception"+message);

    }
}
