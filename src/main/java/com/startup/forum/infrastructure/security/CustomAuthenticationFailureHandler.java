package com.startup.forum.infrastructure.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
//        if(exception instanceof CustomAccessDeniedException) {
//            setDefaultFailureUrl("/api/view/login?ban");
//        }
//        else {
//            setDefaultFailureUrl("/api/view/login?error");
//        }
        super.onAuthenticationFailure(request, response, exception);
    }

    @Override
    public void setDefaultFailureUrl(String defaultFailureUrl) {
        super.setDefaultFailureUrl(defaultFailureUrl);
    }
}
