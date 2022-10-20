package com.crypto.recommendation.common;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CryptoRecord {
    private long timestamp;
    private String code;
    private double price;
}
