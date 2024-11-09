package org.test.task.xm.crypto.recommendation.price;

import org.springframework.boot.SpringApplication;

public class TestXmCryptoLoaderApplication {

    public static void main(String[] args) {
        SpringApplication.from(XmCryptoLoaderApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
