package com.example.springjwtauth.config;

import com.example.springjwtauth.mapper.PostMapper;
import com.example.springjwtauth.mapper.PostMapperImpl;
import com.example.springjwtauth.mapper.UserMapper;
import com.example.springjwtauth.mapper.UserMapperImpl;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class SpringConfig {

    @Bean
    public UserMapper userMapper() {
        return new UserMapperImpl();  //MapStruct Generated source
    }

    @Bean
    public PostMapper postMapper() {
        return new PostMapperImpl(); //MapStruct Generated source
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
                .setConnectTimeout(Duration.ofMillis(3000))
                .setReadTimeout(Duration.ofMillis(3000))
                .build();
    }
}
