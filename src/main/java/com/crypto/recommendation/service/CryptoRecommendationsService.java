package com.crypto.recommendation.service;

import com.crypto.recommendation.common.CryptoNormalizedRange;
import com.crypto.recommendation.common.CryptoPriceStatistic;

import java.util.List;

public interface CryptoRecommendationsService {

    List<CryptoNormalizedRange> getCryptoListSortedByNormalizedRange();

    CryptoPriceStatistic getCryptoPriceStatisticBy(String code);

    String getCryptoCodeWithHighestNormalizedRangeBy(String date);
}
