package com.improveid.gateway.config;


import com.improveid.gateway.filter.AuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Autowired
    private AuthenticationFilter authFilter;

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("user-service", r -> r.path("/user/auth/**")
                        .uri("lb://user-service"))
                .route("user-service", r -> r.path("/user/**")
                        .filters(f -> f.filter(authFilter))
                        .uri("lb://user-service"))
                .route("restaurant-service", r -> r.path("/restaurant/**")
                        .filters(f -> f.filter(authFilter))
                        .uri("lb://restaurant-service"))
                .build();
    }
}