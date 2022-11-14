package com.crypto.recommendation.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.function.Consumer;
import java.util.stream.Collector;

/**
 * Consumer with summary statistic of BigDecimal values
 */
public class BigDecimalSummaryStatistic implements Consumer<BigDecimal> {

    public static final Integer TRANSACTIONS_SCALE = 2;
    public static final RoundingMode HALF_EVEN = RoundingMode.HALF_EVEN;

    private BigDecimal sum = BigDecimal.ZERO.setScale(TRANSACTIONS_SCALE, HALF_EVEN);
    private BigDecimal minimum = BigDecimal.ZERO.setScale(TRANSACTIONS_SCALE, HALF_EVEN);
    private BigDecimal maximum = BigDecimal.ZERO.setScale(TRANSACTIONS_SCALE, HALF_EVEN);
    private int count;

    /**
     * Method provides a Collector with a statistic of BigDecimal values
     * @return collector for summary statistic of BigDecimal values
     */
    public static Collector<BigDecimal, ?, BigDecimalSummaryStatistic> bigDecimalSummaryStatistics() {

        return Collector.of(BigDecimalSummaryStatistic::new,
                BigDecimalSummaryStatistic::accept,
                BigDecimalSummaryStatistic::merge);
    }

    /**
     *
     * {&#064;inheritdoc }
     */
    @Override
    public void accept(BigDecimal t) {
        if (count == 0) {
            firstElementSetup(t);
        } else {
            sum = sum.add(t);
            minimum = minimum.min(t);
            maximum = maximum.max(t);
            count++;
        }
    }

    /**
     *
     * {&#064;inheritdoc }
     */
    public BigDecimalSummaryStatistic merge(BigDecimalSummaryStatistic s) {
        if (s.count > 0) {
            if (count == 0) {
                setupFirstElement(s);
            } else {
                sum = sum.add(s.sum);
                minimum = minimum.min(s.minimum);
                maximum = maximum.max(s.maximum);
                count += s.count;
            }
        }
        return this;
    }

    private void setupFirstElement(BigDecimalSummaryStatistic s) {
        count = s.count;
        sum = s.sum;
        minimum = s.minimum;
        maximum = s.maximum;
    }

    private void firstElementSetup(BigDecimal t) {
        count = 1;
        sum = t;
        minimum = t;
        maximum = t;
    }

    public BigDecimal getAverage() {
        if (count == 0) {
            return BigDecimal.ZERO.setScale(TRANSACTIONS_SCALE, HALF_EVEN);
        }
        return sum.divide(BigDecimal.valueOf(count), TRANSACTIONS_SCALE, HALF_EVEN);
    }

    public BigDecimal getSum() {
        return sum;
    }

    public void setSum(BigDecimal sum) {
        this.sum = sum;
    }

    public BigDecimal getMinimum() {
        return minimum;
    }

    public void setMinimum(BigDecimal minimum) {
        this.minimum = minimum;
    }

    public BigDecimal getMaximum() {
        return maximum;
    }

    public void setMaximum(BigDecimal maximum) {
        this.maximum = maximum;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "BigDecimalSummaryCollector [sum=" + sum + ", minimum=" + minimum + ", maximum=" + maximum + ", count="
                + count + "]";
    }
}

