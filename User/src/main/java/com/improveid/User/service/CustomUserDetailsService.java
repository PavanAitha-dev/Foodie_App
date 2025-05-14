package com.improveid.User.service;

import com.improveid.User.entity.User;
import com.improveid.User.entity.UserProfile;
import com.improveid.User.exception.BadRequestException;
import com.improveid.User.repository.UserProfileRepository;
import com.improveid.User.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;


    public UserDetails loadUserByUsername(String username) {
        List<Object> list=new ArrayList<>();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new BadRequestException("User not found with username: " + username));

        UserProfile userProfile = userProfileRepository.findByLoginId(user.getId())
                .orElseThrow(() -> new BadRequestException("User profile not found for user: " + username));

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                Collections.singleton(new SimpleGrantedAuthority(userProfile.getRole().getRoleName().toUpperCase()))
        );
    }

}