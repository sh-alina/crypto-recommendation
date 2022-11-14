package com.crypto.recommendation.service;

import com.crypto.recommendation.common.CryptoRecord;
import com.crypto.recommendation.repository.CryptoRecordRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Cryptocurrency data Provider with cashing
 */
@Service
public class CachedCryptoRecordsProvider implements CryptoRecordsProvider {
    private final CryptoRecordRepository cryptoRecordRepository;

    public CachedCryptoRecordsProvider(CryptoRecordRepository cryptoRecordRepository) {
        this.cryptoRecordRepository = cryptoRecordRepository;
    }

    /**
     * Method reads cryptocurrency data from file firstly and then provides the data from cash
     *
     * @return cryptocurrency data as a map with a currency code as a key and cryptocurrency data list as a value
     */
    @Cacheable("cryptoRecords")
    public Map<String, List<CryptoRecord>> read() {
        return cryptoRecordRepository.read();
    }
}
