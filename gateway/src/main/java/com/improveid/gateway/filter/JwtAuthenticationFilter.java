package com.improveid.gateway.filter;

import com.improveid.gateway.util.JwtUtil;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;


@Component
public class JwtAuthenticationFilter implements GlobalFilter  { // Changed to WebFilter


    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 1. Skip auth for login endpoint
        if (exchange.getRequest().getPath().toString().startsWith("/user/auth")) {
            return chain.filter(exchange);
        }

        // 2. Validate JWT
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return onError(exchange,"Token not found", HttpStatus.UNAUTHORIZED);
        }

        String token = authHeader.substring(7);
        if (!jwtUtil.validateToken(token)) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return onError(exchange,"Token is invalid", HttpStatus.UNAUTHORIZED);
        }

        // 3. Add user info to headers
        exchange.getRequest().mutate()
                .header("X-User-Id", jwtUtil.extractUsername(token))
                .build();

       /* List<String> roles = List.of("ROLE_ADMIN");

        // Convert roles to GrantedAuthority
        List<SimpleGrantedAuthority> authorities = roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());*/

        // Create authentication object
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(jwtUtil.extractUsername(token), null, null);

        // Set authentication in security context
        return chain.filter(exchange)
                .contextWrite(ReactiveSecurityContextHolder.withAuthentication(auth));

    }


    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus status) {
        exchange.getResponse().setStatusCode(status);
        exchange.getResponse().getHeaders().add(HttpHeaders.CONTENT_TYPE, "application/json");

        String errorResponse = String.format("{\"error\": \"%s\"}", err);
        DataBuffer buffer = exchange.getResponse()
                .bufferFactory()
                .wrap(errorResponse.getBytes(StandardCharsets.UTF_8));

        return exchange.getResponse().writeWith(Mono.just(buffer));
    }
}