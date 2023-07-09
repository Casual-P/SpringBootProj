package com.startup.forum.infrastructure.security.oauth2.impl;

import com.startup.forum.infrastructure.security.oauth2.OauthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class Oauth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final OauthService oauthService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        CustomOauth2UserDetails oAuth2User = (CustomOauth2UserDetails) authentication.getPrincipal();
//        if(oAuth2User.isBanned()) {
//            SecurityContextHolder.clearContext();
//            response.sendRedirect("/api/view/login?ban");
//        }
        super.onAuthenticationSuccess(request, response, authentication);
    }
}
