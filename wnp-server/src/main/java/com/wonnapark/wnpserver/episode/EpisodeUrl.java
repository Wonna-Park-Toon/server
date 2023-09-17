package com.wonnapark.wnpserver.episode;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "episode_urls")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EpisodeUrl {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "episode_id", nullable = false)
    private Episode episode;

    @Column(name = "url", nullable = false)
    private String url;

    public EpisodeUrl(String url) {
        this.url = url;
    }

    public void setEpisode(Episode episode) {
        episode.getEpisodeUrls().add(this);
        this.episode = episode;
    }

}
