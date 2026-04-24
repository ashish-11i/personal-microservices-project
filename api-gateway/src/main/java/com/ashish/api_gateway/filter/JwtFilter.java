package com.ashish.api_gateway.filter;

import com.ashish.api_gateway.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Component
public class JwtFilter implements GlobalFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        String path = exchange.getRequest().getPath().value();

        // Bypass JWT validation for authentication endpoints
        if (path.startsWith("/auth") || path.contains("/users/register")) {
            return chain.filter(exchange);
        }

        String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String token = authHeader.substring(7);
        try {
            jwtUtil.validateToken(token);
            Claims claims = jwtUtil.getClaims(token);
            String role = claims.get("role", String.class);
            String username = claims.getSubject();

            // Role-based validation
            // Example: paths starting with /orders/** are restricted based on role
            // This is where you can add more custom role-based logic
            if (path.startsWith("/orders") && !"ROLE_ADMIN".equals(role)) {
                 exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                 return exchange.getResponse().setComplete();
            }

            // Forward user details to downstream services
            exchange.getRequest().mutate()
                    .header("X-User-Name", username)
                    .header("X-User-Role", role)
                    .build();

        } catch (Exception e) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        return chain.filter(exchange);
    }
}
