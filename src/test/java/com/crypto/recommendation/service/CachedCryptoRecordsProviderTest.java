package com.crypto.recommendation.service;

import com.crypto.recommendation.common.CryptoRecord;
import com.crypto.recommendation.repository.CryptoRecordRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class CachedCryptoRecordsProviderTest {

    @Autowired
    private CachedCryptoRecordsProvider cachedCryptoRecordsProvider;
    @MockBean
    private CryptoRecordRepository cryptoRecordRepository;

    @Test
    void shouldRead() {
        cachedCryptoRecordsProvider.read();
        final Map<String, List<CryptoRecord>> map = cachedCryptoRecordsProvider.read();

        assertNotNull(map);
        verify(cryptoRecordRepository, times(1)).read();
    }
}