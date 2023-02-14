package com.example.springbootproj.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;


@Getter
public class CustomJwtAuthException extends AuthenticationException {
    private HttpStatus status;

    public CustomJwtAuthException(String explanation, HttpStatus status) {
        super(explanation);
        this.status = status;
    }

    public CustomJwtAuthException(String explanation) {
        super(explanation);
    }

    public CustomJwtAuthException(String explanation, Throwable throwable) {
        super(explanation, throwable);
    }
}
