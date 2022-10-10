package com.example.springbootproj.security.oauth2;

import com.example.springbootproj.entity.User;
import com.example.springbootproj.security.oauth2.impl.CustomOauth2UserDetails;

public interface OauthService {
    User save(CustomOauth2UserDetails oAuth2User);

    User update(CustomOauth2UserDetails oAuth2User);

    User delete(CustomOauth2UserDetails oAuth2User);

    User find(CustomOauth2UserDetails oAuth2User);
}
