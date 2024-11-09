package org.test.task.xm.crypto.recommendation.price;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.test.task.xm.crypto.recommendation.price.service.PricesLoadService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = XmCryptoLoaderApplication.class)
class XmCryptoLoaderTests {

    @Autowired
    protected PricesLoadService pricesLoadService;

    @Test
    void contextLoads() {

    }

    @Test
    void load() {
        pricesLoadService.load();
    }

}
