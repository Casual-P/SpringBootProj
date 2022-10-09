package com.example.springjwtauth.mapper;

import com.example.springjwtauth.entity.Role;
import com.example.springjwtauth.entity.User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class OauthMapper {

    public User mapToUser(OAuth2User oAuth2User) {
//        User user = new User();
//        String email = oAuth2User.getAttribute("email");
//        String name = oAuth2User.getAttribute("name");
//        user.setUsername(name);
//        user.setEmail(email);
//        user.setRoles(Collections.singleton(Role.USER));
//        user.setProvider("google");
//        return user;
        return null;
    }
}
