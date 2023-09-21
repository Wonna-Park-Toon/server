package com.wonnapark.wnpserver.episode.dto.request;

import jakarta.validation.constraints.NotBlank;

public record LastEpisodeUrlUpdateRequest( // TODO: String 인데 감쌀 필요 있을까?
        @NotBlank(message = "URL은 공백이나 null일 수 없습니다.")
        String lastEpisodeUrl
) {
}
