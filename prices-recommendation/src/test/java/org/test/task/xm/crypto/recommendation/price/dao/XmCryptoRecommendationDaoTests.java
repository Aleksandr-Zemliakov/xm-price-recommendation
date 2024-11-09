package org.test.task.xm.crypto.recommendation.price.dao;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.test.task.xm.crypto.recommendation.price.XmCryptoPriceApplication;
import org.test.task.xm.crypto.recommendation.price.model.CurrencyStatsDb;
import org.test.task.xm.crypto.recommendation.price.model.PriceNormalizedRange;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = XmCryptoPriceApplication.class)
class XmCryptoRecommendationDaoTests {

    @Autowired
    protected CryptoPriceDao dao;

    @Test
    @Sql( {"/db/insert_test_data.sql"} )
    void get_stats_by_currency() {

        var actual = assertDoesNotThrow( () -> dao.getStatsByCurrency("BTC"));

        assertTrue(actual.isPresent());

        var expected = new CurrencyStatsDb(
                Instant.parse("2022-01-01T00:00:00Z"),
                new BigDecimal(11),
                Instant.parse("2022-01-04T00:00:00Z"),
                new BigDecimal(14),
                Instant.parse("2022-01-03T00:00:00Z"),
                new BigDecimal(1),
                Instant.parse("2022-01-02T00:00:00Z"),
                new BigDecimal(22)
        );

        assertEquals(actual.get(), expected);

    }

    @Test
    @Sql( {"/db/cleanup_test_data.sql"} )
    void stats_by_currency_empty() {
        var actual = assertDoesNotThrow( () -> dao.getStatsByCurrency("BTC"));
        assertTrue(actual.isEmpty());
    }

    @Test
    @Sql( {"/db/cleanup_test_data.sql"} )
    void normalized_range_empty() {
        var actual = assertDoesNotThrow( () -> dao.getNormalizedRange());
        assertTrue(actual.isEmpty());
    }

    @Test
    @Sql( {"/db/insert_test_data.sql"} )
    void normalized_range() {

        var actual = assertDoesNotThrow( () -> dao.getNormalizedRange());

        assertFalse(actual.isEmpty());

        assertEquals(2, actual.size());

        var first = new PriceNormalizedRange("BTC", new BigDecimal("21").setScale(4, RoundingMode.HALF_EVEN));
        var second = new PriceNormalizedRange("ETH", new BigDecimal("1").setScale(4, RoundingMode.HALF_EVEN));

        assertEquals(first, actual.getFirst());
        assertEquals(second, actual.getLast());

    }

    @Test
    @Sql( {"/db/cleanup_test_data.sql"} )
    void highest_range_empty() {
        var actual = assertDoesNotThrow( () -> dao.getHighestRangeCurrency(LocalDate.parse("2024-01-01")));
        assertTrue(actual.isEmpty());
    }

    @Test
    @Sql( {"/db/insert_test_data.sql"} )
    void highest_range() {

        var actual = assertDoesNotThrow( () -> dao.getHighestRangeCurrency(LocalDate.parse("2022-01-01")));

        assertTrue(actual.isPresent());

        var expected = new PriceNormalizedRange("BTC", new BigDecimal("0.1").setScale(4, RoundingMode.HALF_EVEN));

        assertEquals(expected, actual.get());

    }

}
