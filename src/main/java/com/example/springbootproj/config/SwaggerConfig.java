package com.example.springbootproj.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customizeSwagger(List<SecurityRequirement> securityRequirements) {
        return new OpenAPI()
                .security(securityRequirements)
                .info(swaggerInfo());
    }

    @Bean
    public Info swaggerInfo() {
        return new Info()
                .title("Project Swagger API")
                .version("1.0.0")
                .contact(swaggerContact());
    }


    @Bean
    public Contact swaggerContact() {
        return new Contact()
                .name("Anon")
                .email("nafilim29@mail.ru")
                .url("https://github.com/Casual-P");
    }

    @Bean
    public SecurityRequirement swaggerSecurity() {
        return new SecurityRequirement()
                .addList("auth");
    }

}
