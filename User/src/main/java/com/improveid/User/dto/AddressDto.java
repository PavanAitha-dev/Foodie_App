package com.improveid.User.dto;


import lombok.Data;

@Data
public class AddressDto {
    private long id;
    private Long userId;
    private String address;
    private String landMark;
    private String pincode;
    private String addressType;

}
