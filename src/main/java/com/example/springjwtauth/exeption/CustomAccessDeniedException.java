package com.example.springjwtauth.exeption;

public class CustomAccessDeniedException extends CustomAuthenticationException {

    private String message;

    public CustomAccessDeniedException(String message) {
        super(message);
        this.message = message;
    }

    public CustomAccessDeniedException(String message, Throwable cause) {
        super(message, cause);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
