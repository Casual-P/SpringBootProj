package com.example.springjwtauth.security.oauth2.impl;

import com.example.springjwtauth.component.Roles;
import com.example.springjwtauth.entity.User;
import com.example.springjwtauth.repository.UserRepository;
import com.example.springjwtauth.security.oauth2.OauthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class OauthServiceImpl implements OauthService {

    private final UserRepository userRepository;

    @Override
    public User save(CustomOauth2UserDetails oAuth2User) {
        User user = convert(oAuth2User);
        userRepository.save(user);
        return user;
    }

    @Override
    public User update(CustomOauth2UserDetails oAuth2User) {
        User curUser = find(oAuth2User);
        User newUser = convert(oAuth2User);
        curUser.setEmail(newUser.getEmail());
        curUser.setUsername(newUser.getUsername());
        userRepository.save(curUser);
        return newUser;
    }

    private User convert(CustomOauth2UserDetails oAuth2User) {
        User user = new User();
        String email = oAuth2User.getEmail();
        String name = oAuth2User.getAttribute("name");
        String provider = oAuth2User.getProvider();
        user.setUsername(name);
        user.setEmail(email);
        user.setAuth_provider(provider);
        user.setUserOauthId(oAuth2User.getUserOauthId());
        user.setRoles(Collections.singleton(Roles.USER));
        return user;
    }

    @Override
    public User delete(CustomOauth2UserDetails oAuth2User) {
        User curUser = convert(oAuth2User);
        return userRepository.deleteByUserOauthId(curUser.getUserOauthId()).orElseThrow();
    }

    @Override
    public User find(CustomOauth2UserDetails oAuth2User) {
        User user = userRepository.findByUserOauthId(oAuth2User.getUserOauthId()).orElseThrow();
        oAuth2User.setBanned(user.isBanned());
        return user;
    }
}
