package com.wonnapark.wnpserver.episode;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum EpisodeErrorMessage {

    EPISODE_NOT_FOUND("해당 ID(%s)의 에피소드를 찾을 수 없습니다"),
    WEBTOON_NOT_FOUND("해당 ID(%s)의 웹툰을 찾을 수 없습니다"),
    DUPLICATED_EPISODE_TITLE("해당 웹툰 ID(%s)에 동일한 TITLE(%s)의 에피소드가 이미 존재합니다");

    private final String message;

    public String getMessage(Object... wrongCondition) {
        return String.format(message, wrongCondition);
    }

}
