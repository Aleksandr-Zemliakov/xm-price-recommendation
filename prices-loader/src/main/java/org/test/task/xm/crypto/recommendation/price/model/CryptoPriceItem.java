package org.test.task.xm.crypto.recommendation.price.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

public record CryptoPriceItem(Instant timestamp,
                              String name,
                              BigDecimal price) {

    public CryptoPriceItem {

        Objects.requireNonNull(timestamp);
        Objects.requireNonNull(name);
        Objects.requireNonNull(price);

        if (price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Price must be greater than zero");
        }

    }

}

