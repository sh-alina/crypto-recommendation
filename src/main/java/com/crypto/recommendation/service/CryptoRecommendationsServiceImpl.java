package com.crypto.recommendation.service;

import com.crypto.recommendation.common.CryptoNormalizedRange;
import com.crypto.recommendation.common.CryptoPriceStatistic;
import com.crypto.recommendation.common.CryptoRecord;
import com.crypto.recommendation.exception.CryptoDataNotFoundException;
import com.crypto.recommendation.exception.NotSupportedException;
import com.crypto.recommendation.util.BigDecimalSummaryStatistic;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static java.util.Comparator.comparingLong;
import static java.util.stream.Collectors.groupingBy;

/**
 * {@inheritDoc}
 */
@Service
@Slf4j
public class CryptoRecommendationsServiceImpl implements CryptoRecommendationsService {

    private final CryptoRecordsProvider cryptoRecordsProvider;

    public CryptoRecommendationsServiceImpl(CryptoRecordsProvider cryptoRecordsProvider) {
        this.cryptoRecordsProvider = cryptoRecordsProvider;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<CryptoNormalizedRange> getCryptoListSortedByNormalizedRange() {
        return cryptoRecordsProvider.read().entrySet().stream()
                .map(cryptoRecords -> getNormalizedRange(cryptoRecords.getKey(), cryptoRecords.getValue()))
                .sorted(Comparator.comparing(CryptoNormalizedRange::getNormalizedRange).reversed())
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CryptoPriceStatistic getCryptoPriceStatisticBy(String code) {
        final List<CryptoRecord> cryptoRecords = cryptoRecordsProvider.read().get(code);

        if (cryptoRecords == null) {
            log.error("Unsupported crypto code: {}", code);
            throw new NotSupportedException(format("Unsupported crypto code: %s", code));
        }

        BigDecimalSummaryStatistic priceSummaryStatistics = getPriceSummaryStatistics(cryptoRecords);
        final List<CryptoRecord> copyCryptoList = cryptoRecords.stream().sorted(comparingLong(CryptoRecord::getTimestamp))
                .collect(Collectors.toList());
        return new CryptoPriceStatistic(code, priceSummaryStatistics.getMinimum(), priceSummaryStatistics.getMaximum(),
                copyCryptoList.get(0).getPrice(), cryptoRecords.get(copyCryptoList.size() - 1).getPrice());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCryptoCodeWithHighestNormalizedRangeBy(String date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");

        return cryptoRecordsProvider.read().values().stream()
                .flatMap(Collection::stream)
                .filter(cryptoRecord -> date.equals(simpleDateFormat.format(new Date(cryptoRecord.getTimestamp()))))
                .collect(groupingBy(CryptoRecord::getCode)).entrySet().stream()
                .map(cryptoRecords -> getNormalizedRange(cryptoRecords.getKey(), cryptoRecords.getValue()))
                .max(Comparator.comparing(CryptoNormalizedRange::getNormalizedRange))
                .orElseThrow(CryptoDataNotFoundException::new)
                .getCode();
    }

    private CryptoNormalizedRange getNormalizedRange(String cryptoCode, List<CryptoRecord> cryptoRecords) {
        BigDecimalSummaryStatistic bigDecimalSummaryStatistic = getPriceSummaryStatistics(cryptoRecords);

        return new CryptoNormalizedRange(cryptoCode,
                (bigDecimalSummaryStatistic.getMaximum().subtract(bigDecimalSummaryStatistic.getMinimum()))
                        .divide(bigDecimalSummaryStatistic.getMinimum(), RoundingMode.HALF_EVEN));
    }

    private static BigDecimalSummaryStatistic getPriceSummaryStatistics(List<CryptoRecord> cryptoRecords) {
        return cryptoRecords.stream()
                .map(CryptoRecord::getPrice)
                .collect(BigDecimalSummaryStatistic.bigDecimalSummaryStatistics());
    }
}
