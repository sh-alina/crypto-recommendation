package com.crypto.recommendation.service;

import com.crypto.recommendation.common.CryptoRecord;
import com.crypto.recommendation.repository.CryptoRecordRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CachedCryptoRecordsProvider {
    private final CryptoRecordRepository cryptoRecordRepository;

    public CachedCryptoRecordsProvider(CryptoRecordRepository cryptoRecordRepository) {
        this.cryptoRecordRepository = cryptoRecordRepository;
    }
    @Cacheable("cryptoRecords")
    public Map<String, List<CryptoRecord>> read() {
        return cryptoRecordRepository.read();
    }
}
