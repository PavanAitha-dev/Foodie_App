package com.improveId.Order.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class OrderServiceSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtUtil jwtUtil) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("order/activeDeliveries/**").hasAuthority("DELIVERY")
                        .requestMatchers("order/addDelivery/**").hasAuthority("DELIVERY")
                        .requestMatchers("order/allDeliveriesById/**").hasAuthority("DELIVERY")
                        .requestMatchers("order/updateDeliveries/**").hasAuthority("DELIVERY")
                        .requestMatchers("order/add").hasAuthority("CUSTOMER")
                        .requestMatchers("order/getAllOrders/").hasAuthority("COADMIN")
                        .requestMatchers("order/getOrdersCustId/**").hasAuthority("CUSTOMER")
                        .requestMatchers("order/pay/**").hasAuthority("CUSTOMER")
                        .requestMatchers("order/report").hasAuthority("ADMIN")
                        .requestMatchers("order/updateRating/**").hasAuthority("CUSTOMER")
                        .requestMatchers("order/updateStatus/**").hasAnyAuthority("COADMIN", "DELIVERY")
                        .requestMatchers("order/user/**").hasAuthority("CUSTOMER")
                        .requestMatchers("/actuator/**").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(new JwtAuthenticationFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}