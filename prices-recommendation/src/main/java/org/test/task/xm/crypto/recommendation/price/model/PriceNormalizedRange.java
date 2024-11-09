package org.test.task.xm.crypto.recommendation.price.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public record PriceNormalizedRange(String currencyCode,
                                   BigDecimal normalizedPrice) {

    public PriceNormalizedRange {
        Objects.requireNonNull(currencyCode);
        Objects.requireNonNull(normalizedPrice);
    }

}
