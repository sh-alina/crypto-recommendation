package com.crypto.recommendation.common;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class CryptoNormalizedRange {
    private String code;
    private BigDecimal normalizedRange;
}
