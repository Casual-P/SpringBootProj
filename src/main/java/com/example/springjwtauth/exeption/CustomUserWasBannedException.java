package com.example.springjwtauth.exeption;

public class CustomUserWasBannedException extends RuntimeException{

    private String message;

    public CustomUserWasBannedException() {
    }

    public CustomUserWasBannedException(String message) {
        super(message);
        this.message = message;
    }

    public CustomUserWasBannedException(String message, Throwable cause) {
        super(message, cause);
        this.message = message;
    }

    public CustomUserWasBannedException(Throwable cause) {
        super(cause);
    }

    public CustomUserWasBannedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.message = message;
    }
}
