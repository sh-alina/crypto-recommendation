package com.crypto.recommendation.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.NOT_FOUND, reason="No such data.")
public class NotSupportedException extends RuntimeException {

    public NotSupportedException(String message) {
        super(message);
    }
}
