package org.test.task.xm.crypto.recommendation.price.service;

import org.springframework.stereotype.Service;
import org.test.task.xm.crypto.recommendation.price.dao.CryptoPriceDao;
import org.test.task.xm.crypto.recommendation.price.exception.CryptoPriceDataNotFoundException;
import org.test.task.xm.crypto.recommendation.price.model.PriceDetail;
import org.test.task.xm.crypto.recommendation.price.model.PriceNormalizedRange;
import org.test.task.xm.crypto.recommendation.price.model.CurrencyStats;

import java.time.LocalDate;
import java.util.List;

/**
 *
 * Service for providing Crypto Currencies' prices-related information.
 * Calls corresponding methods of {@link CryptoPriceDao}
 *
 * @author Aleksandr Zemliakov
 * @since 0.0.1
 *
 */
@Service
public class CryptoPriceService {

    private final CryptoPriceDao cryptoPriceDao;

    public CryptoPriceService(CryptoPriceDao cryptoPriceDao) {
        this.cryptoPriceDao = cryptoPriceDao;
    }

    /**
     *
     * Returns list of normalized ranges for all currencies present.
     * In case there is no cryptocurrency data in database - returns empty list
     *
     * @author Aleksandr Zemliakov
     * @since 0.0.1
     *
     * @return list (potentially empty) of normalized ranges
     */
    public List<PriceNormalizedRange> getNormalizedRange() {
        return cryptoPriceDao.getNormalizedRange();
    }

    /**
     *
     * Returns Crypto Currency code with highest normalized range for a specified date along with value of normalized range.
     *
     * @author Aleksandr Zemliakov
     * @since 0.0.1
     *
     * @param date value of date
     * @return normalized range
     * @throws CryptoPriceDataNotFoundException Thrown in case there is no data about cryptocurrencies for a specified date
     */
    public PriceNormalizedRange getHighestCurrency(LocalDate date) throws CryptoPriceDataNotFoundException {
        return cryptoPriceDao.getHighestRangeCurrency(date)
                .orElseThrow( () -> new CryptoPriceDataNotFoundException("No data found for [" + date + "] date"));
    }

    /**
     *
     * Returns Crypto Currency statistics for a specified currency code.
     * Statistic include Timestamp and Price for earliest, latest, lowest and highest values
     *
     * @author Aleksandr Zemliakov
     * @since 0.0.1
     *
     * @param currency Crypto Currency code
     * @return statistics on specified cryptocurrency
     * @throws CryptoPriceDataNotFoundException Thrown in case there is no data about specified cryptocurrency
     */
    public CurrencyStats getStats(String currency) throws CryptoPriceDataNotFoundException {
        var result = cryptoPriceDao.getStatsByCurrency(currency)
                .orElseThrow( () -> new CryptoPriceDataNotFoundException("No data found for [" + currency + "] currency code"));
        return new CurrencyStats(currency,
                new PriceDetail(result.tsEarliest(), result.priceEarliest()),
                new PriceDetail(result.tsLatest(), result.priceLatest()),
                new PriceDetail(result.tsOfLow(), result.priceLow()),
                new PriceDetail(result.tsOfHigh(), result.priceHigh())
        );
    }

}
