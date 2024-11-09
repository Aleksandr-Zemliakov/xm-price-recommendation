package org.test.task.xm.crypto.recommendation.price.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.test.task.xm.crypto.recommendation.price.dao.CryptoPriceDao;
import org.test.task.xm.crypto.recommendation.price.exception.CryptoPriceDataNotFoundException;
import org.test.task.xm.crypto.recommendation.price.model.CurrencyStatsDb;
import org.test.task.xm.crypto.recommendation.price.model.PriceNormalizedRange;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CryptoPriceServiceTest {

    private final CryptoPriceDao dao = mock(CryptoPriceDao.class);

    private CryptoPriceService service;

    @BeforeEach
    void setUp() {
        service = new CryptoPriceService(dao);
    }

    @Test
    void getNormalizedRangeEmpty() {
        when(dao.getNormalizedRange()).thenReturn(Collections.emptyList());
        var result = assertDoesNotThrow( () -> service.getNormalizedRange());
        assertEquals(0, result.size());
    }

    @Test
    void getHighestCurrency_Empty() {
        when(dao.getHighestRangeCurrency(any())).thenReturn(Optional.empty());
        assertThrows(CryptoPriceDataNotFoundException.class, () -> service.getHighestCurrency(any()));
    }

    @Test
    void getHighestCurrency() {
        var expected = new PriceNormalizedRange("BTC", new BigDecimal("0.1").setScale(4, RoundingMode.HALF_EVEN));
        when(dao.getHighestRangeCurrency(any())).thenReturn(Optional.of(expected));

        var result = assertDoesNotThrow(() -> service.getHighestCurrency(any()));
        assertEquals(expected, result);
    }

    @Test
    void getStatsEmpty() {
        when(dao.getStatsByCurrency(any())).thenReturn(Optional.empty());
        assertThrows(CryptoPriceDataNotFoundException.class, () -> service.getStats(any()));
    }

    @Test
    void getStats() {
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

        when(dao.getStatsByCurrency(any())).thenReturn(Optional.of(expected));

        var result = assertDoesNotThrow(() -> service.getStats("BTC"));

        assertEquals(expected.tsEarliest(), result.earliest().timestamp());
        assertEquals(expected.priceEarliest(), result.earliest().price());

        assertEquals(expected.tsLatest(), result.latest().timestamp());
        assertEquals(expected.priceLatest(), result.latest().price());

        assertEquals(expected.tsOfLow(), result.low().timestamp());
        assertEquals(expected.priceLow(), result.low().price());

        assertEquals(expected.tsOfHigh(), result.high().timestamp());
        assertEquals(expected.priceHigh(), result.high().price());

    }

}