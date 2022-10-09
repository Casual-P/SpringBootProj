package com.example.springjwtauth.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        setDefaultTargetUrl("/api/view/main");
        setAlwaysUseDefaultTargetUrl(false);
        User user = (User) authentication.getPrincipal();
        super.onAuthenticationSuccess(request, response, authentication);
    }

    @Override
    public void setDefaultTargetUrl(String defaultTargetUrl) {
        super.setDefaultTargetUrl(defaultTargetUrl);
    }

    @Override
    public void setAlwaysUseDefaultTargetUrl(boolean alwaysUseDefaultTargetUrl) {
        super.setAlwaysUseDefaultTargetUrl(alwaysUseDefaultTargetUrl);
    }
}
