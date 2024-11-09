package org.test.task.xm.crypto.recommendation.price;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.test.task.xm.crypto.recommendation.price.service.PricesLoadService;

@SpringBootApplication
@ConfigurationPropertiesScan
public class XmCryptoLoaderApplication implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(XmCryptoLoaderApplication.class);

    private final PricesLoadService pricesLoadService;

    @Autowired
    public XmCryptoLoaderApplication(PricesLoadService pricesLoadService) {
        this.pricesLoadService = pricesLoadService;
    }

    public static void main(String[] args) {
        SpringApplication.run(XmCryptoLoaderApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        logger.info("Starting crypto prices load process");

        pricesLoadService.load();

        logger.info("Crypto prices load process finished");

    }

}
