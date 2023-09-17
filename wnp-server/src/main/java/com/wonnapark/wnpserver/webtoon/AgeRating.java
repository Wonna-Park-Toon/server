package com.wonnapark.wnpserver.webtoon;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum AgeRating {

    ALL("전체이용가", 0),
    OVER_12("12세이용가", 12),
    OVER_15("15세이용가", 15),
    OVER_18("18세이용가", 18);

    private final String ratingName;
    private final int ageLimit;

    AgeRating(String ratingName, int ageLimit) {
        this.ratingName = ratingName;
        this.ageLimit = ageLimit;
    }

    public static AgeRating from(String ratingName) {
        return Arrays.stream(AgeRating.values())
                .filter(v -> v.getRatingName().equals(ratingName))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(String.format("%s와 일치하는 연령등급이 없습니다.", ratingName)));
    }
}
