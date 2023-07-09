package com.startup.forum.infrastructure.security;

import com.startup.forum.infrastructure.filter.EncodePassFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomSecurityConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private final EncodePassFilter encodePassFilter;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.addFilterAfter(encodePassFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
