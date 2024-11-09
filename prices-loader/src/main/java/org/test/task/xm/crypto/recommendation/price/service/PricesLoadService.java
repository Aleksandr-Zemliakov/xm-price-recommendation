package org.test.task.xm.crypto.recommendation.price.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.test.task.xm.crypto.recommendation.price.config.PricesLoaderConfiguration;
import org.test.task.xm.crypto.recommendation.price.dao.PricesLoadDao;
import org.test.task.xm.crypto.recommendation.price.exception.CryptoItemValidationException;
import org.test.task.xm.crypto.recommendation.price.model.CryptoPriceItem;
import org.test.task.xm.crypto.recommendation.price.util.CryptoPriceItemMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * Service responsible for parsing files and loading their contents into database.
 *
 * @author Aleksandr Zemliakov
 * @since 0.0.1
 *
 */
@Service
public class PricesLoadService {

    private static final Logger logger = LoggerFactory.getLogger(PricesLoadService.class);

    private final PricesLoadDao pricesLoadDao;
    private final PricesLoaderConfiguration config;

    @Autowired
    public PricesLoadService(PricesLoadDao pricesLoadDao, PricesLoaderConfiguration config) {
        this.pricesLoadDao = pricesLoadDao;
        this.config = config;
    }

    /**
     *
     * Main method orchestrating file reading, parsing and invoking DAO method responsible for inserting into database.
     * Source folder for files to load is set in prices.loader.source property of configuration files.
     *
     * @author Aleksandr Zemliakov
     * @since 0.0.1
     * @see PricesLoaderConfiguration
     *
     */
    public void load() {

        logger.info("Starting crypto prices load process from [{}] folder", config.source());

        try (var path = Files.list(Path.of(config.source()))) {
            path
                    .filter(file -> !Files.isDirectory(file))
                    .filter(file -> file.getFileName().toString().endsWith(".csv"))
                    .toList()
                    .forEach(file -> {
                        try {
                            pricesLoadDao.insertAll(readFile(file));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     *
     * Utility method for invoking parsing procedure.
     * Operating on a best-effort basis, this method logs underlying parsing errors for further investsigation instead of stopping the procedure entirely.
     *
     * @author Aleksandr Zemliakov
     * @since 0.0.1
     *
     * @param file path to file containing Crypto Prices
     * @return list of successfully parsed {@link CryptoPriceItem} objects
     */
    private List<CryptoPriceItem> readFile(Path file) throws IOException {

        List<CryptoPriceItem> items = new ArrayList<>();

        logger.info("Parsing [{}] file", file);

        try (var lines = Files.lines(file).skip(1).filter(line -> !line.trim().isEmpty())) {
            lines.forEach(line -> {
                try {
                    items.add(CryptoPriceItemMapper.map(line.split(",")));
                } catch (CryptoItemValidationException e) {
                    logger.error("Line validation failed, skipping line [{}]", line);
                }
            });
        }

        logger.info("Finished parsing [{}] file, {} records processed", file, items.size());

        return items;

    }

}
