package com.wonnapark.wnpserver.episode.dto.request;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record EpisodeArtistCommentUpdateRequest(
        @NotBlank(message = "작가의 말은 공백이나 null일 수 없습니다.")
        @Length(min = 1, max = 100, message = "작가의 말은 1자 이상 100자 이하여야 합니다.")
        String artistComment
) {
}
