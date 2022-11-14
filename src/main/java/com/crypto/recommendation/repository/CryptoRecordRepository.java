package com.crypto.recommendation.repository;

import com.crypto.recommendation.common.CryptoRecord;

import java.util.List;
import java.util.Map;

/**
 * Cryptocurrency reader
 */
public interface CryptoRecordRepository {
    /**
     * Method provides cryptocurrency data
     * @return a map of cryptocurrency with a cryptocurrency code as a key and
     * a cryptocurrency data list as a value
     */
    Map<String, List<CryptoRecord>> read();
}
