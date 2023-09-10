package com.wonnapark.wnpserver.episode.presentation;

import com.wonnapark.wnpserver.episode.application.EpisodeFindUseCase;
import com.wonnapark.wnpserver.episode.dto.request.WebtoonListPageRequest;
import com.wonnapark.wnpserver.episode.dto.response.EpisodeDetailFormResponse;
import com.wonnapark.wnpserver.episode.dto.response.EpisodeListFormResponse;
import com.wonnapark.wnpserver.global.response.ApiResponse;
import com.wonnapark.wnpserver.global.response.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/v1/user/episode")
@RequiredArgsConstructor
public class UserEpisodeController {

    private final EpisodeFindUseCase episodeFindUseCase;

    @GetMapping("/{episodeId}/detail")
    @ResponseStatus(OK)
    ApiResponse<EpisodeDetailFormResponse> findEpisodeDetailForm(@PathVariable Long episodeId) {
        Long userId = 1L; // TODO: parameter에 UserInfo 추가하고 삭제
        EpisodeDetailFormResponse episodeDetailForm = episodeFindUseCase.findEpisodeDetailForm(userId, episodeId);
        return ApiResponse.from(episodeDetailForm);
    }

    @GetMapping("/list")
    @ResponseStatus(OK)
    ApiResponse<PageResponse<EpisodeListFormResponse>> findEpisodeListForm(
            @RequestParam Long webtoonId,
            WebtoonListPageRequest webtoonListPageRequest
    ) {
        Long userId = 1L; // TODO: parameter에 UserInfo 추가하고 삭제
        PageResponse<EpisodeListFormResponse> episodeListForms = PageResponse.from(episodeFindUseCase.findEpisodeListForm(userId, webtoonId, webtoonListPageRequest.toPageable()));
        return ApiResponse.from(episodeListForms);
    }

}