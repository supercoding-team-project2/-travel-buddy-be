package com.github.travelbuddy.users.service;

import com.github.travelbuddy.users.dto.CustomUserDetails;
import com.github.travelbuddy.users.entity.UserEntity;
import com.github.travelbuddy.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        UserEntity userData = userRepository.findByEmail(email);

        if(userData != null) {
            return new CustomUserDetails(userData.getId(), userData.getEmail(), userData.getPassword());
        }else {
            throw new UsernameNotFoundException(email);
        }

    }
}
