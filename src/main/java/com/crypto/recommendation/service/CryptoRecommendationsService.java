package com.crypto.recommendation.service;

import com.crypto.recommendation.common.CryptoNormalizedRange;
import com.crypto.recommendation.common.CryptoPriceStatistic;

import java.util.List;

/**
 * Service provides methods for providing some information and statistic for cryptocurrencies.
 */
public interface CryptoRecommendationsService {

    /**
     * Provides a crypto list with sorting by normalized range
     * @return a crypto list
     */
    List<CryptoNormalizedRange> getCryptoListSortedByNormalizedRange();

    /**
     * Provides a Summary Statistic for defined currency code
     * @param code a currency code as string
     * @return summary statistic for provided code
     */
    CryptoPriceStatistic getCryptoPriceStatisticBy(String code);

    /**
     * Returns a crypto code with the highest normalized ranges for defined date.
     * @param date a date in format 'dd.MM.YYYY'
     * @return a crypto code with the highest normalized ranges
     */
    String getCryptoCodeWithHighestNormalizedRangeBy(String date);
}
