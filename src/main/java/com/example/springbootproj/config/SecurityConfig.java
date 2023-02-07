package com.example.springbootproj.config;

import com.example.springbootproj.security.CustomAuthenticationFailureHandler;
import com.example.springbootproj.security.CustomSecurityConfigurer;
import com.example.springbootproj.security.oauth2.impl.CustomOauth2UserService;
import com.example.springbootproj.security.oauth2.impl.CustomOauthTokenConverter;
import com.example.springbootproj.security.oauth2.impl.Oauth2LoginSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.http.OAuth2ErrorResponseErrorHandler;
import org.springframework.security.oauth2.core.http.converter.OAuth2AccessTokenResponseHttpMessageConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomSecurityConfigurer configurer;

    private final UserDetailsService userDetailsService;

    private final CustomOauth2UserService oauth2UserService;

    private final Oauth2LoginSuccessHandler oauth2LoginSuccessHandler;

    private final CustomAuthenticationFailureHandler oAuthFailureHandler;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        return daoAuthenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf()
                .and()
                .oauth2Login()
                .loginPage("/api/auth/login")
                .permitAll()
                .userInfoEndpoint()
                .userService(oauth2UserService)
                .and()
                .tokenEndpoint()
                .accessTokenResponseClient(accessTokenResponseClient())
                .and()
                .defaultSuccessUrl("/api/view/main", false)
                .and()
                .httpBasic()
                .and()
                .formLogin()
                .loginPage("/api/auth/login")
                .permitAll()
                .defaultSuccessUrl("/api/view/main", false)
                .failureUrl("/api/auth/login?error")
                .permitAll()
                .and()
                .rememberMe()
                .useSecureCookie(true)
                .and()
                .logout()
                .permitAll()
                .logoutUrl("/api/auth/logout")
                .and()
                .authorizeRequests()
                .mvcMatchers("/api/view/main", "/api/view/about",
                        "/css/**", "/js/**", "/images/**", "/font/**",
                        "/oauth2/**", "/api/auth/**")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .build();
    }


    @Bean
    public OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> accessTokenResponseClient(){
        DefaultAuthorizationCodeTokenResponseClient accessTokenResponseClient =
                new DefaultAuthorizationCodeTokenResponseClient();
        OAuth2AccessTokenResponseHttpMessageConverter tokenResponseHttpMessageConverter =
                new OAuth2AccessTokenResponseHttpMessageConverter();
        tokenResponseHttpMessageConverter.setAccessTokenResponseConverter(new CustomOauthTokenConverter());
        RestTemplate restTemplate = new RestTemplate(Arrays.asList(
                new FormHttpMessageConverter(), tokenResponseHttpMessageConverter));
        restTemplate.setErrorHandler(new OAuth2ErrorResponseErrorHandler());
        accessTokenResponseClient.setRestOperations(restTemplate);
        return accessTokenResponseClient;
    }

    //    @Bean
//    public UserDetailsService userDetailsService() {
//        InMemoryUserDetailsManager inMemoryUserDetailsManager = new InMemoryUserDetailsManager();
//        UserDetails userDetails = User.builder().username("admin").password("$2y$10$LzOhtsWn1hbcBaqWXzsNaOQQE0gUE1XivphL5eDbCg5S0U2FbDN9m").roles("ADMIN").build();
//        inMemoryUserDetailsManager.createUser(userDetails);
//        return inMemoryUserDetailsManager;
//    }
}


