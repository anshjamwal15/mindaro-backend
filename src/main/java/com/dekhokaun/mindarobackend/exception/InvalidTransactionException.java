package com.dekhokaun.mindarobackend.exception;

import org.springframework.http.HttpStatus;

public class InvalidTransactionException extends ApiException {
  public InvalidTransactionException(String message) {
    super(message, HttpStatus.BAD_REQUEST);
  }
}
