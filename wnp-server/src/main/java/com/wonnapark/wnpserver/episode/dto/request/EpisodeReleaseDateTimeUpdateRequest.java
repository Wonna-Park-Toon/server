package com.wonnapark.wnpserver.episode.dto.request;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record EpisodeReleaseDateTimeUpdateRequest(
        @NotNull(message = "공개일은 null일 수 없습니다.")
        LocalDateTime releaseDateTime
) {
}
