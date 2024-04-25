package org.example.shortsaccount.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlayHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "play_id", updatable = false)
    private Long playId;

    @Column(name = "video_id", nullable = false)
    private int videoId;

    @Column(name = "play_date", columnDefinition = "TIMESTAMP")
    private LocalDateTime playDate;

    @Column(name = "ad_views", nullable = false)
    private int adViews;

    @Builder
    public PlayHistory(int videoId, int adViews) {
        this.videoId = videoId;
        this.adViews = adViews;
        this.playDate = LocalDateTime.now();
    }
}
