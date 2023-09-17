package com.wonnapark.wnpserver.global.response;

import lombok.Getter;

@Getter
public class ApiResponse<T> {
    private final T data;

    private ApiResponse(T data) {
        this.data = data;
    }

    public static <T> ApiResponse<T> from(T data) {
        return new ApiResponse<>(data);
    }

}
