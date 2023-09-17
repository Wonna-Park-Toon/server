package com.wonnapark.wnpserver.global.response;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.List;

public record PageResponse<T>(
        List<T> content,
        long numberOfElements,
        int page,
        int size,
        Sort sort,
        int totalPages,
        long totalElements
) {
    public static <T> PageResponse<T> from(Page<T> page) {
        return new PageResponse<>(
                page.getContent(),
                page.getNumberOfElements(),
                page.getPageable().getPageNumber(),
                page.getPageable().getPageSize(),
                page.getPageable().getSort(),
                page.getTotalPages(),
                page.getTotalElements()
        );
    }
}
