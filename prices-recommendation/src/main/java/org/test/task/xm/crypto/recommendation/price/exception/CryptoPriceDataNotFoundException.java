package org.test.task.xm.crypto.recommendation.price.exception;

/**
 *
 * Exception indicating Crypto Price data is not available for a given request parameters
 *
 * @author Aleksandr Zemliakov
 * @since 0.0.1
 *
 */
public class CryptoPriceDataNotFoundException extends Exception {

    public CryptoPriceDataNotFoundException(String message) {
        super(message);
    }

}
