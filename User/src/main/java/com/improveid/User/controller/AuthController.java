package com.improveid.User.controller;

import com.improveid.User.dto.LoginRequest;
import com.improveid.User.service.CustomUserDetailsService;
import com.improveid.User.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/auth")
public class AuthController {

    @Autowired
    private JwtUtil jwtTokenUtil;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest request) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        return jwtTokenUtil.generateToken(userDetails);
    }
}
