package com.wonnapark.wnpserver.config;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;

public class TestContainerConfig implements BeforeAllCallback {

    private static final String REDIS_IMAGE = "redis:7.0.8-alpine";
    private static final int REDIS_PORT = 6379;
    private GenericContainer redis;

    @Override
    public void beforeAll(ExtensionContext context) {
        redis = new GenericContainer(REDIS_IMAGE)
                .withExposedPorts(REDIS_PORT)
                .waitingFor(Wait.forListeningPort()); // 컨테이너 띄워졌는지 확인 후 실행
        redis.start();
        System.setProperty("spring.data.redis.host", redis.getHost());
        System.setProperty("spring.data.redis.port", String.valueOf(redis.getMappedPort(REDIS_PORT)));
    }

}
