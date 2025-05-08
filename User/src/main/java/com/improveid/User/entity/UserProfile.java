package com.improveid.User.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Table(name = "user_profile")
@Data
public class UserProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @NotNull(message = "Login reference is required")
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "login_id")
    private User login;

    @NotNull(message = "Role is required")
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @NotNull(message = "Full name is required")
    @Size(max = 100, message = "Full name must be less than 100 characters")
    private String fullName;

    @NotNull(message = "Email is required")
    @Email(message = "Email should be valid")
    @Column(unique = true)
    private String email;
}