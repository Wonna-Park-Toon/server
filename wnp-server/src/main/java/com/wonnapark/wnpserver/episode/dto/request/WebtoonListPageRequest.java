package com.wonnapark.wnpserver.episode.dto.request;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

public record WebtoonListPageRequest(
        Integer page,
        Direction direction
) {

    public static final int DEFAULT_PAGE = 0;
    public static final int DEFAULT_SIZE = 20;
    public static final Direction DEFAULT_DIRECTION = Direction.DESC;
    public static final String SORTING_CRITERIA = "createAt";


    public Pageable toPageable() {
        return PageRequest.of(
                page == null ? DEFAULT_PAGE : page,
                DEFAULT_SIZE,
                Sort.by(
                        direction == null ? DEFAULT_DIRECTION : direction,
                        SORTING_CRITERIA
                )
        );
    }

}
