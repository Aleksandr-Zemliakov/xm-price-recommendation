package org.test.task.xm.crypto.recommendation.price.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.test.task.xm.crypto.recommendation.price.model.CryptoPriceItem;

import java.sql.Timestamp;
import java.util.List;

@Component
public class PricesLoadDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PricesLoadDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void insertAll(List<CryptoPriceItem> items) {

        jdbcTemplate.batchUpdate(
                """
                insert into crypto_price(price_ts, currency_code, price)
                values (?,?,?)
                on conflict(price_ts, currency_code) do nothing""",
                items,
                100,
                (ps, arg) -> {
                    ps.setTimestamp(1, Timestamp.from(arg.timestamp()));
                    ps.setString(2, arg.name());
                    ps.setObject(3, arg.price());
                }
        );

    }

}
