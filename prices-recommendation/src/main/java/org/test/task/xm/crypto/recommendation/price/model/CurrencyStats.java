package org.test.task.xm.crypto.recommendation.price.model;

import java.util.Objects;

public record CurrencyStats(String name,
                            PriceDetail earliest,
                            PriceDetail latest,
                            PriceDetail low,
                            PriceDetail high) {

    public CurrencyStats {

        Objects.requireNonNull(name);
        Objects.requireNonNull(earliest);
        Objects.requireNonNull(latest);
        Objects.requireNonNull(low);
        Objects.requireNonNull(high);

    }

}
