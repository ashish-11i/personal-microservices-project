package com.ashish.api_gateway.filter;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.ashish.api_gateway.utils.JwtUtil;

import io.jsonwebtoken.Claims;
import reactor.core.publisher.Mono;

@Component
public class JwtFilter implements GlobalFilter, Ordered {

    @Autowired
    private JwtUtil jwtUtil;

    // Routes that do not require a JWT token
    private static final List<String> PUBLIC_PATHS = List.of(
            "/auth/login",
            "/users/register"
    );

    @Override
    public int getOrder() {
        return -1; // run before routing
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        String path   = exchange.getRequest().getPath().value();
        HttpMethod method = exchange.getRequest().getMethod();

        // Block internal service endpoints — these are only for service-to-service calls
        // and must never be reachable from outside via the gateway.
        if (path.startsWith("/users/internal")) {
            exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
            return exchange.getResponse().setComplete();
        }

        // Allow public routes through without a token
        boolean isPublic = PUBLIC_PATHS.stream().anyMatch(path::startsWith)
                || (path.startsWith("/users/register") && HttpMethod.POST.equals(method));

        if (isPublic) {
            return chain.filter(exchange);
        }

        // All other routes require a valid Bearer token
        String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String token = authHeader.substring(7);

        try {
            jwtUtil.validateToken(token);
            Claims claims = jwtUtil.getClaims(token);

            // Forward caller identity to downstream services so they can use it if needed
            exchange = exchange.mutate()
                    .request(r -> r
                            .header("X-User-Name", claims.getSubject())
                            .header("X-User-Role", claims.get("role", String.class)))
                    .build();

        } catch (Exception e) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        return chain.filter(exchange);
    }
}
