package com.crypto.recommendation.repository;

import com.crypto.recommendation.common.CryptoRecord;

import java.util.List;
import java.util.Map;

public interface CryptoRecordRepository {
    Map<String, List<CryptoRecord>> read();
}
