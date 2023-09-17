package com.wonnapark.wnpserver.episode.dto.request;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record EpisodeTitleUpdateRequest(
        @NotBlank(message = "제목은 공백이나 null일 수 없습니다.")
        @Length(min = 1, max = 35, message = "제목은 1자 이상 35자 이하여야 합니다.")
        String title
) {
}
