package com.improveId.Order.circuitBreaker;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {

    @Bean
    public CircuitBreaker feignCircuitBreaker(CircuitBreakerRegistry registry) {
        return registry.circuitBreaker("feignCircuitBreaker");
    }

    @Bean
    public CustomFeignErrorDecoder errorDecoder() {
        return new CustomFeignErrorDecoder();
    }
}
