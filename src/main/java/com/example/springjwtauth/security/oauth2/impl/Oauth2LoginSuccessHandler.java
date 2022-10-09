package com.example.springjwtauth.security.oauth2;

import com.example.springjwtauth.service.OauthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.NoSuchElementException;

@Component
@RequiredArgsConstructor
public class Oauth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final OauthService oauthService;

    private final SecurityContextHolder securityContextHolder;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        securityContextHolder
        CustomOauth2UserDetails oAuth2User = (CustomOauth2UserDetails) authentication.getPrincipal();
        try {
            oauthService.find(oAuth2User);
            oauthService.update(oAuth2User);
        }catch (NoSuchElementException ex) {
            System.out.println();
            oauthService.save(oAuth2User);
        }
        super.onAuthenticationSuccess(request, response, authentication);
    }
}
