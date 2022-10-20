package com.crypto.recommendation.common;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CryptoPriceStatistic {
    private String code;
    private double min;
    private double max;
    private double oldest;
    private double newest;
}
