package com.example.springjwtauth.service.impl;

import com.example.springjwtauth.entity.User;
import com.example.springjwtauth.exeption.CustomAccessDeniedException;
import com.example.springjwtauth.exeption.CustomAuthenticationException;
import com.example.springjwtauth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseGet(() -> userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User not Found")));
        return org.springframework.security.core.userdetails.User
                .builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRoles().stream().map(Enum::name).toArray(String[]::new))
                .disabled(user.isBanned())
                .build();
    }
}
