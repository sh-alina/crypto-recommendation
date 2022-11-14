package com.crypto.recommendation.rest;

import com.crypto.recommendation.common.CryptoNormalizedRange;
import com.crypto.recommendation.common.CryptoPriceStatistic;
import com.crypto.recommendation.service.CryptoRecommendationsService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
public class RecommendationController {

    private final CryptoRecommendationsService cryptoRecommendationsService;

    public RecommendationController(CryptoRecommendationsService cryptoRecommendationsService) {
        this.cryptoRecommendationsService = cryptoRecommendationsService;
    }

    @ApiOperation(value = "The endpoint will return a descending sorted list of all the cryptos, comparing the normalized range (i.e. (max-min)/min).",
            code = 200)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Success", response = List.class),
            @ApiResponse(code = 404, message = "Not Found - There is no data"),
            @ApiResponse(code = 500, message = "Internal Service Error")})
    @GetMapping("/cryptos-by-normalized-range")
    public ResponseEntity<List<CryptoNormalizedRange>> getCryptoListSortedByNormalizedRange() {
        return ResponseEntity.ok(cryptoRecommendationsService.getCryptoListSortedByNormalizedRange());
    }

    @ApiOperation(value = "The endpoint will return the oldest/newest/min/max values for a requested crypto code.",
            code = 200)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Success", response = CryptoPriceStatistic.class),
            @ApiResponse(code = 404, message = "Not Found - There is no data"),
            @ApiResponse(code = 500, message = "Internal Service Error")})
    @GetMapping("/cryptos-price-statistic")
    public ResponseEntity<CryptoPriceStatistic> getCryptoPriceStatisticBy(@RequestParam(value = "code",
            defaultValue = "BTC", required = true) String code) {
        return ResponseEntity.ok(cryptoRecommendationsService.getCryptoPriceStatisticBy(code));
    }

    @ApiOperation(value = "The endpoint will return the crypto code with the highest normalized range for a specific date.",
            code = 200)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Success", response = String.class),
            @ApiResponse(code = 404, message = "Not Found - There is no data"),
            @ApiResponse(code = 500, message = "Internal Service Error")})
    @GetMapping("/crypto-with-highest-normalized-range")
    public ResponseEntity<String> getCryptoWithTheHighestNormalizedRangeBy(
            @RequestParam(value = "date", defaultValue = "01.01.2022", required = true) String date) {
        return ResponseEntity.ok(cryptoRecommendationsService.getCryptoCodeWithHighestNormalizedRangeBy(date));
    }
}
