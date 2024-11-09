package org.test.task.xm.crypto.recommendation.price.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.test.task.xm.crypto.recommendation.price.exception.CryptoPriceDataNotFoundException;
import org.test.task.xm.crypto.recommendation.price.model.PriceNormalizedRange;
import org.test.task.xm.crypto.recommendation.price.model.CurrencyStats;
import org.test.task.xm.crypto.recommendation.price.service.CryptoPriceService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = "/api/price", produces = MediaType.APPLICATION_JSON_VALUE)
public class CryptoPricesController {

    private final CryptoPriceService priceService;

    public CryptoPricesController(CryptoPriceService priceService) {
        this.priceService = priceService;
    }

    @GetMapping(value = "/")
    @Operation(summary = "Get Normalized Range",
    description = """
        Returns normalized range for all crypto currencies present in database across whole period.
        Currencies are sorted according to normalized range in descending order.
    """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successfully retrieved normalized range",
                    content = @Content(
                    examples = {
                            @ExampleObject(
                                    name = "Success with data",
                                    description = "List of currencies along with their normalized price",
                                    value = """
[
    {
        "currencyCode": "ETH",
        "normalizedPrice": 0.6383
    },
    {
        "currencyCode": "XRP",
        "normalizedPrice": 0.5060
    },
    {
        "currencyCode": "DOGE",
        "normalizedPrice": 0.5046
    },
    {
        "currencyCode": "LTC",
        "normalizedPrice": 0.4651
    },
    {
        "currencyCode": "BTC",
        "normalizedPrice": 0.4341
    }
]
                                            """
                            ),
                            @ExampleObject(
                                    name = "Empty response",
                                    description = "Empty response indicating no data is present for any currency",
                                    value = "[]"
                            )
                    }
            )
            )
    })
    public ResponseEntity<List<PriceNormalizedRange>> getRange() {
        return ResponseEntity.ok(priceService.getNormalizedRange());
    }

    @GetMapping(value = "/{date}")
    @Operation(
            summary = "Get highest currency for date",
            description = "Returns crypto currency with the highest normalized range for a specific day."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successfully retrieved currency",
                    content = @Content(
                            examples = @ExampleObject(
                                    value = """
                                            {
                                            "currencyCode": "ETH",
                                            "normalizedPrice": 0.6383
                                            }"""
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Crypto price data is not present for a specified day",
                    content = @Content( mediaType = MediaType.TEXT_PLAIN_VALUE,
                            examples = @ExampleObject(
                            summary = "Example summary",
                            value = "No data found for [2021-01-01] date"
                    ))
            )
    })
    public ResponseEntity<PriceNormalizedRange> getHighestCurrencyForDate(
            @PathVariable
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            @Parameter(
                    in = ParameterIn.PATH,
                    name = "date",
                    description = "Date in YYYY-MM-DD format",
                    required = true,
                    content = @Content( examples = @ExampleObject(value = "2022-01-01"))
            )
            final LocalDate date
    ) throws CryptoPriceDataNotFoundException {
        return ResponseEntity.ok(priceService.getHighestCurrency(date));
    }

    @GetMapping(value = "/stats/{currency}")
    @Operation(
            summary = "Get crypto currency statistics",
            description = "Returns specific crypto currency statistics: oldest/newest/min/max values"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successfully retrieved statistics",
                    content = @Content(
                            examples = @ExampleObject(
                                    value = """
{
    "name": "BTC",
    "earliest": {
        "timestamp": "2022-01-01T00:00:00Z",
        "price": 46813.21
    },
    "latest": {
        "timestamp": "2022-01-31T16:00:00Z",
        "price": 38415.79
    },
    "low": {
        "timestamp": "2022-01-24T07:00:00Z",
        "price": 33276.59
    },
    "high": {
        "timestamp": "2022-01-01T20:00:00Z",
        "price": 47722.66
    }
}"""
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Crypto statistics data is not present for a specified currency",
                    content = @Content( mediaType = MediaType.TEXT_PLAIN_VALUE,
                            examples = @ExampleObject(
                                    summary = "Example summary",
                                    value = "No data found for [KOTE] currency"
                            ))
            )
    })
    public ResponseEntity<CurrencyStats> getCurrencyStats(
            @PathVariable
            @Parameter(
                    in = ParameterIn.PATH,
                    name = "currency",
                    description = "Crypto currency code",
                    required = true,
                    content = @Content( examples = @ExampleObject(value = "BTC"))
            )
            final String currency
    ) throws CryptoPriceDataNotFoundException {
        return ResponseEntity.ok(priceService.getStats(currency));
    }

    @ExceptionHandler( { CryptoPriceDataNotFoundException.class } )
    public ResponseEntity<?> handleException(final Exception exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

}
