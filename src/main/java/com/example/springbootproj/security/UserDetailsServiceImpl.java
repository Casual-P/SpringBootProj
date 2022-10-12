package com.example.springbootproj.security;

import com.example.springbootproj.entity.Role;
import com.example.springbootproj.entity.User;
import com.example.springbootproj.repository.UserRepository;
import com.example.springbootproj.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseGet(() -> userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User not Found")));
        return CustomUserDetails.builder()
                .id(user.getId())
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRoles().stream().map(Role::getId).map(Enum::name).toArray(String[]::new))
                .disabled(user.isBanned())
                .build();
    }
}
