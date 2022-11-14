package com.crypto.recommendation.rest;

import com.crypto.recommendation.common.CryptoNormalizedRange;
import com.crypto.recommendation.common.CryptoPriceStatistic;
import com.crypto.recommendation.service.CryptoRecommendationsService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(RecommendationController.class)
class RecommendationControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CryptoRecommendationsService cryptoRecommendationsService;
    private static final Integer TRANSACTIONS_SCALE = 2;
    private static final RoundingMode HALF_EVEN = RoundingMode.HALF_EVEN;

    @Test
    void getCryptoListSortedByNormalizedRange() throws Exception {
        final BigDecimal normalizedRange = BigDecimal.valueOf(213.01).setScale(TRANSACTIONS_SCALE, HALF_EVEN);
        Mockito.when(cryptoRecommendationsService.getCryptoListSortedByNormalizedRange())
                .thenReturn(List.of(new CryptoNormalizedRange("AAA", normalizedRange)));

        mockMvc.perform(get("/cryptos-by-normalized-range"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].code", Matchers.is("AAA")))
                .andExpect(jsonPath("$[0].normalizedRange").value(normalizedRange));
    }

    @Test
    void getCryptoPriceStatisticBy() throws Exception {
        Mockito.when(cryptoRecommendationsService.getCryptoPriceStatisticBy("AAA"))
                .thenReturn(new CryptoPriceStatistic("AAA", BigDecimal.valueOf(1234.4), BigDecimal.valueOf(1567.9), BigDecimal.valueOf(1234.4), BigDecimal.valueOf(1567.9)));

        mockMvc.perform(get("/cryptos-price-statistic").queryParam("code", "AAA"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("AAA"))
                .andExpect(jsonPath("$.min").value(1234.4))
                .andExpect(jsonPath("$.max").value(1567.9))
                .andExpect(jsonPath("$.oldest").value(1234.4))
                .andExpect(jsonPath("$.newest").value(1567.9)
                );
    }

    @Test
    void getCryptoWithTheHighestNormalizedRangeBy() throws Exception {
        Mockito.when(cryptoRecommendationsService.getCryptoCodeWithHighestNormalizedRangeBy("01.01.2022"))
                .thenReturn("XTB");

        mockMvc.perform(get("/crypto-with-highest-normalized-range"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("XTB"));
    }
}