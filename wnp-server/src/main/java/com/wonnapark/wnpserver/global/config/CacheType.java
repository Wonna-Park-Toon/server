package com.wonnapark.wnpserver.global.config;

import lombok.Getter;

import static com.wonnapark.wnpserver.global.config.CacheType.ConstConfig.DEFAULT_EXPIRATION_TIME;
import static com.wonnapark.wnpserver.global.config.CacheType.ConstConfig.DEFAULT_MAX_SIZE;

@Getter
public enum CacheType {
    WEBTOONS_BY_PUBLISH_DAY_VIEW_COUNT("webtoonsByPublishDayOrderByViewCount", DEFAULT_EXPIRATION_TIME, DEFAULT_MAX_SIZE),
    WEBTOONS_BY_PUBLISH_DAY_POPULARITY("webtoonsByPublishDayOrderByPopularity", DEFAULT_EXPIRATION_TIME, DEFAULT_MAX_SIZE);

    public final String cacheName;
    private final int secsToExpireAfterWrite;
    private final int maximumSize;

    CacheType(String cacheName, int secsToExpireAfterWrite, int maximumSize) {
        this.cacheName = cacheName;
        this.secsToExpireAfterWrite = secsToExpireAfterWrite;
        this.maximumSize = maximumSize;
    }

    static class ConstConfig {
        static final int DEFAULT_EXPIRATION_TIME = 60 * 60 * 6;
        static final int DEFAULT_MAX_SIZE = 7;
    }
}
