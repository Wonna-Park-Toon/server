package com.wonnapark.wnpserver.oauth.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.wonnapark.wnpserver.oauth.OAuthProvider;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class NaverInfoResponse implements OAuthInfoResponse {

    private Response response;

    @Getter
    @NoArgsConstructor
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Response {
        private String id;
        private String nickname;
        private String gender;
        private String birthyear;
    }

    @Override
    public String getProviderId() {
        return response.id;
    }

    @Override
    public String getNickname() {
        return response.nickname;
    }

    @Override
    public OAuthProvider getOAuthProvider() {
        return OAuthProvider.NAVER;
    }

    @Override
    public int getAge() {
        return getAgeFromBirthYear(response.birthyear);
    }

    @Override
    public String getGender() {
        return response.gender;
    }

    private int getAgeFromBirthYear(String birthYear) {
        int currentYear = LocalDateTime.now().getYear();
        int userBirthYear = Integer.parseInt(birthYear);
        return currentYear - userBirthYear;
    }
}
