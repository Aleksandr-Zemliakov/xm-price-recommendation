package org.test.task.xm.crypto.recommendation.price.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 *
 * Configuration for Crypto Prices Loader Application
 *
 * @author Aleksandr Zemliakov
 * @since 0.0.1
 *
 * @param source path to folder containing Crypto Prices files
 */
@ConfigurationProperties(prefix = "prices.loader")
public record PricesLoaderConfiguration(String source) { }
