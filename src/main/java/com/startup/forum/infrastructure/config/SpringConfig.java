package com.startup.forum.infrastructure.config;

import com.startup.forum.infrastructure.mapper.PostMapper;
import com.startup.forum.infrastructure.mapper.PostMapperImpl;
import com.startup.forum.infrastructure.mapper.UserMapper;
import com.startup.forum.infrastructure.mapper.UserMapperImpl;
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
