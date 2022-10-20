package com.crypto.recommendation.common;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CryptoNormalizedRange {
    private String code;
    private double normalizedRange;
}
