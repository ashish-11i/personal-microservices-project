package com.ashish.order_service.client;

import com.ashish.order_service.dto.UserDto;
import org.springframework.stereotype.Component;

// Triggered when user-service is DOWN (circuit open). Returns a safe placeholder
// so order-service does not crash — the order flow will still fail at business
// validation if needed, but gracefully with a proper error message.
@Component
public class UserClientFallback implements UserClient {

    @Override
    public UserDto getUser(Long id) {
        return new UserDto(id, "Unknown", "Unknown", "unknown@example.com", null, null);
    }
}
