package com.wonnapark.wnpserver.episode.dto.request;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record EpisodeThumbnailUpdateRequest(
        @NotBlank(message = "썸네일은 공백이나 null일 수 없습니다.")
        @Length(max = 255, message = "썸네일 길이는 255자 이하여야 합니다.")
        String thumbnail
) {
}
