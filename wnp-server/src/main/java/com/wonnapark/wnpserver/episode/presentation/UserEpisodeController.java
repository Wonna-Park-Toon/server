package com.wonnapark.wnpserver.episode.presentation;

import com.wonnapark.wnpserver.episode.application.EpisodeFindUseCase;
import com.wonnapark.wnpserver.episode.dto.request.WebtoonListPageRequest;
import com.wonnapark.wnpserver.episode.dto.response.EpisodeDetailFormResponse;
import com.wonnapark.wnpserver.episode.dto.response.EpisodeListFormResponse;
import com.wonnapark.wnpserver.global.auth.Authorized;
import com.wonnapark.wnpserver.global.common.UserInfo;
import com.wonnapark.wnpserver.global.response.ApiResponse;
import com.wonnapark.wnpserver.global.response.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/v1/user/episode")
@RequiredArgsConstructor
public class UserEpisodeController {

    private final EpisodeFindUseCase episodeFindUseCase;

    @GetMapping("/{episodeId}/detail")
    @ResponseStatus(OK)
    public ApiResponse<EpisodeDetailFormResponse> findEpisodeDetailForm(
            @Authorized UserInfo userInfo,
            @PathVariable Long episodeId
    ) {
        EpisodeDetailFormResponse episodeDetailForm = episodeFindUseCase.findEpisodeDetailForm(userInfo.userId(), episodeId);
        return ApiResponse.from(episodeDetailForm);
    }

    @GetMapping("/list")
    @ResponseStatus(OK)
    public ApiResponse<PageResponse<EpisodeListFormResponse>> findEpisodeListForm(
            @Authorized UserInfo userInfo,
            @RequestParam Long webtoonId,
            @ModelAttribute WebtoonListPageRequest webtoonListPageRequest
    ) {
        PageResponse<EpisodeListFormResponse> episodeListForms = PageResponse.from(episodeFindUseCase.findEpisodeListForm(userInfo.userId(), webtoonId, webtoonListPageRequest.toPageable()));
        return ApiResponse.from(episodeListForms);
    }

}
