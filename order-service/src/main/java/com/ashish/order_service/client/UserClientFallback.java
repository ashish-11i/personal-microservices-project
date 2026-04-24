package com.ashish.order_service.client;

import com.ashish.order_service.dto.UserDto;
import org.springframework.stereotype.Component;

@Component
public class UserClientFallback implements UserClient {

    @Override
    public UserDto getUser(Long id) {
        return new UserDto(id, "Unknown", "unknown@example.com");
    }

}

// Interview me bol sakte ho:

// “I have implemented fallback mechanism for fault tolerance”
