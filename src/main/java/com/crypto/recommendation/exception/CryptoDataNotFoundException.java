package com.crypto.recommendation.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "No crypto data is found.")
public class CryptoDataNotFoundException extends RuntimeException {
    public CryptoDataNotFoundException() {
        super();
    }
}
