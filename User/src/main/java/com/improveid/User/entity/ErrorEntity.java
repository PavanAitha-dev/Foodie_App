package com.improveid.User.entity;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ErrorEntity {
    private int status;
    private String error;
    private String message;
}
