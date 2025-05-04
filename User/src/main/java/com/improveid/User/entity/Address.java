package com.improveid.User.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Table(name = "customer_address")
@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String address;
    private String landMark;
    private String pincode;
    private AddressType addressType;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserProfile user;
}