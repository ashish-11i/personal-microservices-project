package com.ashish.auth_service.services;

import java.util.Collections;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ashish.auth_service.client.UserClient;
import com.ashish.auth_service.dto.ExternalUserDto;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserClient userClient;

    public CustomUserDetailsService(UserClient userClient) {
        this.userClient = userClient;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ExternalUserDto userDto = userClient.getUserByEmail(username);
        
        if (userDto == null) {
            throw new UsernameNotFoundException("User not found with email: " + username);
        }

        return new User(
            userDto.getEmail(),
            userDto.getPassword(),
            Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + userDto.getRole()))
        );
    }
}
