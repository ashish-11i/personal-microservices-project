package com.ashish.auth_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ashish.auth_service.dto.ExternalUserDto;

@FeignClient(name = "user-service")
public interface UserClient {

    // Calls the internal endpoint that returns the hashed password.
    // This route is blocked at the API Gateway — only reachable service-to-service.
    @GetMapping("/users/internal/email")
    ExternalUserDto getUserByEmail(@RequestParam("email") String email);
}
