package org.test.task.xm.crypto.recommendation.price;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@ConfigurationPropertiesScan
@EnableCaching
public class XmCryptoPriceApplication {

    private static final Logger logger = LoggerFactory.getLogger(XmCryptoPriceApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(XmCryptoPriceApplication.class, args);
    }

}
