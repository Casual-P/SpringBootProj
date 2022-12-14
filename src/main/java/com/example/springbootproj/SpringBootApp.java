package com.example.springbootproj;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@SecurityScheme(name = "auth", type = SecuritySchemeType.APIKEY, in = SecuritySchemeIn.COOKIE)
@EnableScheduling
public class SpringBootApp {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootApp.class, args);
    }

}
