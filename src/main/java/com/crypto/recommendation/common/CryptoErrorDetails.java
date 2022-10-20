package com.crypto.recommendation.common;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CryptoErrorDetails {
    private long errorCode;
    private String errorMessage;
}
