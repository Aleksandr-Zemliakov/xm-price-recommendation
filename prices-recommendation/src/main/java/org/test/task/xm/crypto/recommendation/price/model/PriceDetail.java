package org.test.task.xm.crypto.recommendation.price.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

public record PriceDetail(Instant timestamp,
                          BigDecimal price) {

    public PriceDetail {

        Objects.requireNonNull(timestamp);
        Objects.requireNonNull(price);

        if (price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Price must be greater than zero");
        }

    }

}
