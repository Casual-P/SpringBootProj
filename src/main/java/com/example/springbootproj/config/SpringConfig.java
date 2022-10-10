package com.example.springbootproj.config;

import com.example.springbootproj.mapper.PostMapper;
import com.example.springbootproj.mapper.PostMapperImpl;
import com.example.springbootproj.mapper.UserMapper;
import com.example.springbootproj.mapper.UserMapperImpl;
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
