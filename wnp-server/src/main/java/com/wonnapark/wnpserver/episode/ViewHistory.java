package com.wonnapark.wnpserver.episode;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Table(name = "view_historys")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ViewHistory {

    @Embeddable
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @EqualsAndHashCode
    public static class ViewHistoryId implements Serializable {

        @Column(name = "user_id", nullable = false)
        private Long userId;

        @Column(name = "episode_id", nullable = false)
        private Long episodeId;

        private ViewHistoryId(Long userId, Long episodeId) {
            this.userId = userId;
            this.episodeId = episodeId;
        }

    }

    @EmbeddedId
    private ViewHistoryId id;

    @Builder
    private ViewHistory(Long userId, Long episodeId) {
        id = new ViewHistoryId(userId, episodeId);
    }

}
