package com.wonnapark.wnpserver.webtoon.exception;

public class UnderageAccessDeniedException extends RuntimeException {
    public UnderageAccessDeniedException(String message) {
        super(message);
    }
}
