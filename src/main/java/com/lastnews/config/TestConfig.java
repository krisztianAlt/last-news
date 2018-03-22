package com.lastnews.config;

import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix="acme")
public class TestConfig {

    private String testVariable;

}
