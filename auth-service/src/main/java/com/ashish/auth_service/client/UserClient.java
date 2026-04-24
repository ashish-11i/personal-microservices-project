package com.ashish.auth_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ashish.auth_service.dto.ExternalUserDto;

@FeignClient(name = "user-service")
public interface UserClient {

    @GetMapping("/users/email")
    ExternalUserDto getUserByEmail(@RequestParam("email") String email);
}
