package org.test.task.xm.crypto.recommendation.price.model;

import java.math.BigDecimal;
import java.time.Instant;

public record CurrencyStatsDb(Instant tsEarliest,
                              BigDecimal priceEarliest,
                              Instant tsLatest,
                              BigDecimal priceLatest,
                              Instant tsOfLow,
                              BigDecimal priceLow,
                              Instant tsOfHigh,
                              BigDecimal priceHigh) {
}
