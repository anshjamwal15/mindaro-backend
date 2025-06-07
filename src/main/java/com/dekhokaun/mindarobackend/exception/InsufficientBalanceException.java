package com.dekhokaun.mindarobackend.exception;

import org.springframework.http.HttpStatus;

public class InsufficientBalanceException extends ApiException {
    public InsufficientBalanceException() {
        super("Insufficient balance", HttpStatus.BAD_REQUEST);
    }

    public InsufficientBalanceException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
