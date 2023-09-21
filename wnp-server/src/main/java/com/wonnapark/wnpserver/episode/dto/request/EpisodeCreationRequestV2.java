package com.wonnapark.wnpserver.episode.dto.request;

import com.wonnapark.wnpserver.episode.Episode;
import com.wonnapark.wnpserver.webtoon.Webtoon;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

public record EpisodeCreationRequestV2(
        @NotBlank(message = "제목은 공백이나 null일 수 없습니다.")
        @Length(min = 1, max = 35, message = "제목은 1자 이상 35자 이하여야 합니다.")
        String title,
        @NotNull(message = "공개일은 null일 수 없습니다.")
        LocalDateTime releaseDateTime,
        @NotBlank(message = "썸네일은 공백이나 null일 수 없습니다.")
        @Length(max = 255, message = "썸네일 길이는 255자 이하여야 합니다.")
        String thumbnail,
        @NotBlank(message = "작가의 말은 공백이나 null일 수 없습니다.")
        @Length(min = 1, max = 100, message = "작가의 말은 1자 이상 100자 이하여야 합니다.")
        String artistComment,
        @Valid
        @NotNull(message = "에피소드 URL은 null일 수 없습니다.")
        String lastEpisodeUrl
) {

    public Episode toEntity(Webtoon webtoon) {
        return Episode.builder()
                .title(title)
                .releaseDateTime(releaseDateTime)
                .thumbnail(thumbnail)
                .artistComment(artistComment)
                .webtoon(webtoon)
                .build();
    }

}
