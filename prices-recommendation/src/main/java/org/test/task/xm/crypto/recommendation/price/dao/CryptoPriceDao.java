package org.test.task.xm.crypto.recommendation.price.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Component;
import org.test.task.xm.crypto.recommendation.price.model.PriceNormalizedRange;
import org.test.task.xm.crypto.recommendation.price.model.CurrencyStatsDb;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Component
public class CryptoPriceDao {

    private final JdbcClient jdbcClient;

    @Autowired
    public CryptoPriceDao(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public List<PriceNormalizedRange> getNormalizedRange() {
        return jdbcClient.sql(
                """
                    select currency_code, round((max(price) - min(price)) / min(price), 4) as normalized_price
                    from crypto_price
                    group by currency_code
                    order by normalized_price desc""")
                .query(PriceNormalizedRange.class)
                .list();
    }

    public Optional<PriceNormalizedRange> getHighestRangeCurrency(LocalDate date) {
        return   jdbcClient.sql(
                """
                        select currency_code, round((max(price) - min(price)) / min(price), 4) as normalized_price
                        from crypto_price
                        where price_ts >= to_timestamp(:dateFrom, 'YYYY/MM/DD') and price_ts < to_timestamp(:dateTo, 'YYYY/MM/DD')
                        group by currency_code
                        order by normalized_price desc
                        limit 1
                """)
                .param("dateFrom", date.format(DateTimeFormatter.ofPattern("yyyy/MM/dd")))
                .param("dateTo", date.plusDays(1).format(DateTimeFormatter.ofPattern("yyyy/MM/dd")))
                .query(PriceNormalizedRange.class)
                .optional();
    }

    public Optional<CurrencyStatsDb> getStatsByCurrency(String currency) {
        return jdbcClient.sql(
                        """
                            select min(price_ts) over() as ts_earliest,
                                   first_value(price) over (order by price_ts asc) as price_earliest,
                                   max(price_ts) over() as ts_latest,
                                   first_value(price) over (order by price_ts desc) as price_latest,
                                   first_value(price_ts) over (order by price asc) as ts_of_low,
                                   min(price) over() as price_low,
                                   first_value(price_ts) over (order by price desc) as ts_of_high,
                                   max(price) over() as price_high
                            from crypto_price
                            where currency_code = :currencyCode
                            limit 1
                        """)
                .param("currencyCode", currency)
                .query(CurrencyStatsDb.class)
                .optional();
    }

}
