package com.example.springbootproj.security.oauth2.impl;

import com.example.springbootproj.component.Roles;
import com.example.springbootproj.entity.Role;
import com.example.springbootproj.entity.User;
import com.example.springbootproj.exception.UserNotFoundException;
import com.example.springbootproj.repository.UserRepository;
import com.example.springbootproj.security.oauth2.OauthService;
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
        String name = oAuth2User.getName();
        String provider = oAuth2User.getProvider();
        user.setUsername(name);
        user.setEmail(email);
        user.setAuth_provider(provider);
        user.setUserOauthId(oAuth2User.getUserOauthId());
        user.setRoles(Collections.singleton(new Role(Roles.USER)));
        return user;
    }

    @Override
    public User delete(CustomOauth2UserDetails oAuth2User) {
        User curUser = convert(oAuth2User);
        User user = userRepository.findByUserOauthId(curUser.getUserOauthId()).orElseThrow(UserNotFoundException::new);
        userRepository.deleteByUserOauthId(curUser.getUserOauthId());
        return user;
    }

    @Override
    public User find(CustomOauth2UserDetails oAuth2User) {
        User user = userRepository.findByUserOauthId(oAuth2User.getUserOauthId()).orElseThrow();
        oAuth2User.setBanned(user.getIsBanned());
        return user;
    }
}
