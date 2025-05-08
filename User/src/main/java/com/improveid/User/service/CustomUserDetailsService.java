package com.improveid.User.service;

import com.improveid.User.entity.User;
import com.improveid.User.entity.UserProfile;
import com.improveid.User.exception.NotFoundException;
import com.improveid.User.repository.UserProfileRepository;
import com.improveid.User.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserProfileRepository userProfileRepository;

    // Spring calls this during login to check credentials
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = null;
        Optional<UserProfile> userProfile=null;
        try {
            user = userRepository.findByUsername(username).orElseThrow(() -> new NotFoundException("User not found"));
            userProfile=userProfileRepository.findByLoginId(user.getId());
            if ((userProfile.isEmpty())){
                throw new NotFoundException(" user not found "+user.getUsername());
            }
        } catch (NotFoundException e) {
            throw new RuntimeException(e);
        }

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                Collections.singleton(new SimpleGrantedAuthority(userProfile.get().getRole().getRoleName()))
        );
    }
}
