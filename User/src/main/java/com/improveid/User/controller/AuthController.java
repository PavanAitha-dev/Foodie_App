package com.improveid.User.controller;

import com.improveid.User.dto.LoginRequest;
import com.improveid.User.dto.RegisterRequest;
import com.improveid.User.dto.UserProfileDto;
import com.improveid.User.service.CustomUserDetailsService;
import com.improveid.User.service.UserService;
import com.improveid.User.util.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/user/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
//        Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
//        );
//
//        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//        String token = jwtUtil.generateToken(userDetails);
//        return ResponseEntity.ok(Map.of("token", token));

            UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());

            // Manual password check
            if (!request.getPassword().equals(userDetails.getPassword())) {
                throw new BadCredentialsException("Invalid password");
            }
            String token = jwtUtil.generateToken(userDetails);

            UserProfileDto userProfileDto=userService.login(request);
            userProfileDto.setToken(token);
            return ResponseEntity.ok(userProfileDto);

    }
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) throws Exception {
        return ResponseEntity.ok(userService.register(request));
    }
}