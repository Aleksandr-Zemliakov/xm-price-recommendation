package org.test.task.xm.crypto.recommendation.price.util;

import org.test.task.xm.crypto.recommendation.price.exception.CryptoItemValidationException;
import org.test.task.xm.crypto.recommendation.price.model.CryptoPriceItem;

import java.math.BigDecimal;
import java.time.DateTimeException;
import java.time.Instant;
import java.util.Arrays;
import java.util.Objects;

/**
 *
 * Utility class for mapping of raw source fields into a valid {@link CryptoPriceItem}
 *
 * @author Aleksandr Zemliakov
 * @since 0.0.1
 *
 */
public class CryptoPriceItemMapper {

    /**
     *
     * Utility method for mapping of raw source fields into a valid {@link CryptoPriceItem}
     *
     * @author Aleksandr Zemliakov
     * @since 0.0.1
     *
     * @param input array of strings representing raw source fields read from file
     *
     * @return valid {@link CryptoPriceItem}
     * @throws CryptoItemValidationException Thrown in case source fields cannot be converted into valid valid {@link CryptoPriceItem}
     *
     */
    public static CryptoPriceItem map(String[] input) throws CryptoItemValidationException {

        Objects.requireNonNull(input);

        if (input.length != 3) throw new CryptoItemValidationException("Should be least 3 items in line");

        var timestamp = parseTimestamp(input[0]);

        var name = input[1];
        if (name == null) throw new CryptoItemValidationException("Symbol must not be empty");

        var price = parsePrice(input[2]);

        return new CryptoPriceItem(timestamp, name, price);

    }

    private static Instant parseTimestamp(String input) throws CryptoItemValidationException {

        if (input == null) throw new CryptoItemValidationException("Timestamp must not be empty");

        long timestampLong;
        try {
            timestampLong = Long.parseLong(input);
        } catch (NumberFormatException e) {
            throw new CryptoItemValidationException("Timestamp must be an integer");
        }

        Instant timestamp;
        try {
            timestamp = Instant.ofEpochMilli(timestampLong);
        } catch (DateTimeException e) {
            throw new CryptoItemValidationException("Cannot parse timestamp");
        }

        return timestamp;

    }

    private static BigDecimal parsePrice(String input) throws CryptoItemValidationException {

        if (input == null) throw new CryptoItemValidationException("Price must not be empty");

        BigDecimal price;
        try {
            price = new BigDecimal(input);
        } catch (NumberFormatException e) {
            throw new CryptoItemValidationException("Price cannot be converted to number");
        }

        return price;

    }

}
