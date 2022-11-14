package com.crypto.recommendation.service;

import com.crypto.recommendation.common.CryptoRecord;

import java.util.List;
import java.util.Map;

/**
 * Provider of cryptocurrencies
 */
public interface CryptoRecordsProvider {
    /**
     * Method reads cryptocurrency data.
     *
     * @return cryptocurrency data as a map with a currency code as a key and cryptocurrency data as a value
     */
    Map<String, List<CryptoRecord>> read();
}
