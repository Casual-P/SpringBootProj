package com.example.springjwtauth.exeption;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;

@Getter
public class CustomAuthenticationException extends AuthenticationException {

    private final String message;

    private HttpStatus status;

    public CustomAuthenticationException(String message, Throwable cause) {
        super(message, cause);
        this.message = message;
    }

    public CustomAuthenticationException(String message, Throwable cause, HttpStatus status) {
        super(message, cause);
        this.message = message;
        this.status = status;
    }

    public CustomAuthenticationException(String message) {
        super(message);
        this.message = message;
    }

    public CustomAuthenticationException(String message, HttpStatus status) {
        super(message);
        this.message = message;
        this.status = status;
    }


}
