package com.crypto.recommendation.service;

import com.crypto.recommendation.common.CryptoNormalizedRange;
import com.crypto.recommendation.common.CryptoPriceStatistic;
import com.crypto.recommendation.common.CryptoRecord;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class CryptoRecommendationsServiceImplTest {
    @Autowired
    private CryptoRecommendationsService cryptoRecommendationsService;
    @MockBean
    private CachedCryptoRecordsProvider cachedCryptoRecordsProvider;

    @Test
    void shouldGetCryptoListSortedByNormalizedRange() {
        when(cachedCryptoRecordsProvider.read()).thenReturn(getTestMap());
        final List<CryptoNormalizedRange> expectedList = List.of(
                new CryptoNormalizedRange("BBB", 0.9738372093023259),
                new CryptoNormalizedRange("AAA", 0.27017174335709654),
                new CryptoNormalizedRange("CCC", 0.005768692425171931)
        );
        final List<CryptoNormalizedRange> cryptoListSortedByNormalizedRange = cryptoRecommendationsService.getCryptoListSortedByNormalizedRange();

        assertEquals(expectedList, cryptoListSortedByNormalizedRange);
    }

    @Test
    void shouldDontGetCryptoListSortedByNormalizedRange() {
        when(cachedCryptoRecordsProvider.read()).thenReturn(Collections.emptyMap());

        final List<CryptoNormalizedRange> cryptoListSortedByNormalizedRange = cryptoRecommendationsService.getCryptoListSortedByNormalizedRange();

        assertEquals(0, cryptoListSortedByNormalizedRange.size());
    }

    @Test
    void shouldGetCryptoListSortedByNormalizedRangeForTheSameValuesInData() {
        when(cachedCryptoRecordsProvider.read()).thenReturn(getTestMapWithTheSameValues());
        final List<CryptoNormalizedRange> expectedList = List.of(
                new CryptoNormalizedRange("BBB", 0.0),
                new CryptoNormalizedRange("AAA", 0.0)
        );
        final List<CryptoNormalizedRange> cryptoListSortedByNormalizedRange = cryptoRecommendationsService.getCryptoListSortedByNormalizedRange();
        assertThat(expectedList).hasSameElementsAs(cryptoListSortedByNormalizedRange);
    }

    @Test
    void shouldGetCryptoPriceStatisticBy() {
        when(cachedCryptoRecordsProvider.read()).thenReturn(getTestMap());
        final CryptoPriceStatistic expected = new CryptoPriceStatistic("AAA", 1234.4, 1567.9, 1234.4, 1567.9);
        final CryptoPriceStatistic result = cryptoRecommendationsService.getCryptoPriceStatisticBy("AAA");

        assertEquals(expected, result);
    }

    @Test
    void shouldGetCryptoCodeWithHighestNormalizedRangeBy() {
        when(cachedCryptoRecordsProvider.read()).thenReturn(getTestMap());
        String expected = "BBB";
        final String result = cryptoRecommendationsService.getCryptoCodeWithHighestNormalizedRangeBy("01.01.2022");

        assertEquals(expected, result);
    }


    private Map<String, List<CryptoRecord>> getTestMap() {
        return Map.of("AAA", List.of(
                        new CryptoRecord(1641009600000L, "AAA", 1234.40),
                        new CryptoRecord(1641009610000L, "AAA", 1345.40),
                        new CryptoRecord(1641009630000L, "AAA", 1567.90)
                ),
                "BBB", List.of(
                        new CryptoRecord(1641009600000L, "BBB", 34.40),
                        new CryptoRecord(1641009610000L, "BBB", 45.9),
                        new CryptoRecord(1641009630000L, "BBB", 67.90)
                ),
                "CCC", List.of(
                        new CryptoRecord(1641009600000L, "CCC", 95342.23),
                        new CryptoRecord(1641009610000L, "CCC", 95342.23),
                        new CryptoRecord(1641009630000L, "CCC", 95892.23)
                )
        );
    }

    private Map<String, List<CryptoRecord>> getTestMapWithTheSameValues() {
        return Map.of("AAA", List.of(
                        new CryptoRecord(1641009600000L, "AAA", 1234.40),
                        new CryptoRecord(1641009610000L, "AAA", 1234.40),
                        new CryptoRecord(1641009630000L, "AAA", 1234.40)
                ),
                "BBB", List.of(
                        new CryptoRecord(1641009600000L, "BBB", 1234.40),
                        new CryptoRecord(1641009610000L, "BBB", 1234.40),
                        new CryptoRecord(1641009630000L, "BBB", 1234.40)
                )
        );
    }
}