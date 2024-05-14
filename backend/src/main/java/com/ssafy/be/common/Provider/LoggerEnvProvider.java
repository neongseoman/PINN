package com.ssafy.be.common.Provider;

import org.springframework.beans.factory.annotation.Value;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class LoggerEnvProvider {

    @Value("${spring.data.mongodb.host}")
    private String mongoHost;
    @Value("${spring.data.mongodb.port}")
    private String mongoPort;
    @Value("${spring.data.mongodb.username}")
    private String mongoUser;
    @Value("${spring.data.mongodb.password}")
    private String mongoPass;

    @PostConstruct
    public void init() {
        System.setProperty("MONGO_HOST", mongoHost);
        System.setProperty("MONGO_PORT", mongoPort);
    }
}
