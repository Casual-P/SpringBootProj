package com.example.springjwtauth;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@SecurityScheme(name = "auth", type = SecuritySchemeType.APIKEY, in = SecuritySchemeIn.COOKIE)
public class SpringJwTauthApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringJwTauthApplication.class, args);
    }

}
