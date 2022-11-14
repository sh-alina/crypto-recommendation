package com.crypto.recommendation.common;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class CryptoRecord {
    private long timestamp;
    private String code;
    private BigDecimal price;
}
