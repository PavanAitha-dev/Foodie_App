package com.improveId.Order.circuitBreaker;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import jakarta.ws.rs.ServiceUnavailableException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Map;
@Slf4j
@Component
public class GlobalFeignFallbackFactory implements FallbackFactory<Object> {

    private final CircuitBreakerRegistry circuitBreakerRegistry;

    public GlobalFeignFallbackFactory(CircuitBreakerRegistry circuitBreakerRegistry) {
        this.circuitBreakerRegistry = circuitBreakerRegistry;
    }

    @Override
    public Object create(Throwable cause) {
        return new GlobalFeignFallback(cause, circuitBreakerRegistry);
    }

    private static class GlobalFeignFallback {
        private final Throwable cause;
        private final CircuitBreakerRegistry circuitBreakerRegistry;

        public GlobalFeignFallback(Throwable cause, CircuitBreakerRegistry circuitBreakerRegistry) {
            this.cause = cause;
            this.circuitBreakerRegistry = circuitBreakerRegistry;
        }
        // This method will be called for any Feign client method
        public Object fallback(Method method, Object[] args) {
            String serviceName = method.getDeclaringClass().getSimpleName()
                    .replace("FeignClient", "")
                    .replace("Client", "");

            log.info(serviceName+"down");
            // Check circuit breaker state
            CircuitBreaker cb = circuitBreakerRegistry.circuitBreaker(serviceName);
            String cbState = cb.getState().name();

            if (cb.getState() == CircuitBreaker.State.OPEN) {
                log.info("Circuit breaker is open");
                throw new ServiceUnavailableException(
                        String.format("%s service circuit breaker is OPEN. Requests are blocked.", serviceName));
            }

            // Log the error and return fallback response
            return createFallbackResponse(serviceName, method.getName(), cbState);
        }

        private Object createFallbackResponse(String serviceName, String methodName, String cbState) {
            // Implement your fallback logic here
            // Could return a default value, cached response, or throw exception
            log.info("Creating fallback response");
            return Map.of(
                    "status", "SERVICE_UNAVAILABLE",
                    "service", serviceName,
                    "method", methodName,
                    "circuitBreakerState", cbState,
                    "message", "Service is temporarily unavailable"
            );
        }
    }
}