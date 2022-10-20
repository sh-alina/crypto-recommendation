package com.crypto.recommendation.service;

import com.crypto.recommendation.common.CryptoNormalizedRange;
import com.crypto.recommendation.common.CryptoPriceStatistic;
import com.crypto.recommendation.common.CryptoRecord;
import com.crypto.recommendation.exception.CryptoDataNotFoundException;
import com.crypto.recommendation.exception.NotSupportedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static java.util.Comparator.comparingLong;
import static java.util.stream.Collectors.groupingBy;

@Service
@Slf4j
public class CryptoRecommendationsServiceImpl implements CryptoRecommendationsService {

    private final CachedCryptoRecordsProvider cachedCryptoRecordsProvider;

    public CryptoRecommendationsServiceImpl(CachedCryptoRecordsProvider cachedCryptoRecordsProvider) {
        this.cachedCryptoRecordsProvider = cachedCryptoRecordsProvider;
    }

    @Override
    public List<CryptoNormalizedRange> getCryptoListSortedByNormalizedRange() {
        return cachedCryptoRecordsProvider.read().entrySet().stream()
                .map(cryptoRecords -> getNormalizedRange(cryptoRecords.getKey(), cryptoRecords.getValue()))
                .sorted(Comparator.comparingDouble(CryptoNormalizedRange::getNormalizedRange).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public CryptoPriceStatistic getCryptoPriceStatisticBy(String code) {
        final List<CryptoRecord> cryptoRecords = cachedCryptoRecordsProvider.read().get(code);

        if (cryptoRecords == null) {
            log.error("Unsupported crypto code: {}", code);
            throw new NotSupportedException(format("Unsupported crypto code: %s", code));
        }

        DoubleSummaryStatistics priceSummaryStatistics = getPriceSummaryStatistics(cryptoRecords);
        final List<CryptoRecord> copyCryptoList = cryptoRecords.stream().sorted(comparingLong(CryptoRecord::getTimestamp))
                .collect(Collectors.toList());
        return new CryptoPriceStatistic(code, priceSummaryStatistics.getMin(), priceSummaryStatistics.getMax(),
                copyCryptoList.get(0).getPrice(), cryptoRecords.get(copyCryptoList.size() - 1).getPrice());
    }

    @Override
    public String getCryptoCodeWithHighestNormalizedRangeBy(String date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");

        return cachedCryptoRecordsProvider.read().values().stream()
                .flatMap(Collection::stream)
                .filter(cryptoRecord -> date.equals(simpleDateFormat.format(new Date(cryptoRecord.getTimestamp()))))
                .collect(groupingBy(CryptoRecord::getCode)).entrySet().stream()
                .map(cryptoRecords -> getNormalizedRange(cryptoRecords.getKey(), cryptoRecords.getValue()))
                .max(Comparator.comparingDouble(CryptoNormalizedRange::getNormalizedRange))
                .orElseThrow(CryptoDataNotFoundException::new)
                .getCode();
    }

    private CryptoNormalizedRange getNormalizedRange(String cryptoCode, List<CryptoRecord> cryptoRecords) {
        DoubleSummaryStatistics doubleSummaryStatistics = getPriceSummaryStatistics(cryptoRecords);

        return new CryptoNormalizedRange(cryptoCode,
                (doubleSummaryStatistics.getMax() - doubleSummaryStatistics.getMin()) /
                        doubleSummaryStatistics.getMin());
    }

    private static DoubleSummaryStatistics getPriceSummaryStatistics(List<CryptoRecord> cryptoRecords) {
        return cryptoRecords.stream()
                .mapToDouble(CryptoRecord::getPrice)
                .summaryStatistics();
    }
}
