package com.ashish.user_service.dto;

import com.ashish.user_service.models.User;

public record UserResponseDto(
        Long id,
        String firstName,
        String lastName,
        String email,
        String phone,
        String role,
        String address,
        String city,
        String state,
        String pincode,
        boolean isActive
) {
    public static UserResponseDto from(User user) {
        return new UserResponseDto(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhone(),
                user.getRole(),
                user.getAddress(),
                user.getCity(),
                user.getState(),
                user.getPincode(),
                user.isActive()
        );
    }
}
