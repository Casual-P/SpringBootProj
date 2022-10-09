package com.example.springjwtauth.service;

import com.example.springjwtauth.entity.User;
import com.example.springjwtauth.security.oauth2.CustomOauth2UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

public interface OauthService {
    User save(CustomOauth2UserDetails oAuth2User);

    User update(CustomOauth2UserDetails oAuth2User);

    User delete(CustomOauth2UserDetails oAuth2User);

    User find(CustomOauth2UserDetails oAuth2User);
}
