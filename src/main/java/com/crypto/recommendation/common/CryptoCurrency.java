package com.crypto.recommendation.common;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString
@EqualsAndHashCode
public final class CryptoCurrency {

    private String code;
    private int fraction;
}
