package org.test.task.xm.crypto.recommendation.price.dao;

import org.springframework.boot.SpringApplication;
import org.test.task.xm.crypto.recommendation.price.XmCryptoPriceApplication;

public class TestXmCryptoRecommendationApplication {

    public static void main(String[] args) {
        SpringApplication.from(XmCryptoPriceApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
