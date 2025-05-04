package com.improveid.User.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    public String username;
    public String password;
    public String fullName;
    public String email;
    public String role;
}

