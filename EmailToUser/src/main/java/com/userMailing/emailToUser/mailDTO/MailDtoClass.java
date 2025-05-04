package com.userMailing.emailToUser.mailDTO;


import lombok.Data;

@Data
public class MailDtoClass {
    private String fullName;
    private String restaurantName;
    private double amount;
    private String mailId;
    private Long orderId;



}
