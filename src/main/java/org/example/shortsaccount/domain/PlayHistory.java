package org.example.shortsaccount.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class PlayHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "play_id", updatable = false)
    private Long playId;

    @Column(name = "video_id", nullable = false)
    private int videoId;

    @Column(name = "user_id", nullable = false)
    private int userId;

    @Column(name = "last_watch_time", nullable = false)
    private int lastWatchTime;

    @Column(name = "play_date", nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime playDate;

    @Builder
    public PlayHistory(int videoId, int userId, int lastWatchTime){
        this.videoId = videoId;
        this.userId = userId;
        this.lastWatchTime = lastWatchTime;
        this.playDate = LocalDateTime.now();
    }

    public void update(int lastWatchTime) {
        this.lastWatchTime = lastWatchTime;
    }

}