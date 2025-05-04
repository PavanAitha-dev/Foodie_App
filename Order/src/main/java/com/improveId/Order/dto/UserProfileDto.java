package com.improveId.Order.dto;

import lombok.Data;

@Data
public class UserProfileDto {
    public Long userID;
    public String username;
    public String fullName;
    public String email;
    public Long roleID;
}
