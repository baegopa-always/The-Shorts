package org.example.shortsaccount.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Statistics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stats_id", updatable = false)
    private int statsId;

    @Column(name = "video_id", nullable = false)
    private int videoId;

    @Column(name = "stats_amount", nullable = false)
    private int statsAmount;

    @Column(name = "stats_date", nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDate statsDate;

    @Column(name = "daily_views", nullable = false)
    private int dailyViews;

    @Column(name = "daily_ad_views", nullable = false)
    private int dailyAdViews;

    @Column(name = "daily_playtime", nullable = false)
    private int dailyPlaytime;

    @Column(name = "user_id", nullable = false)
    private int userId;

    @Builder
    public Statistics(int videoId, int userId, int statsAmount, int dailyViews, int dailyAdViews, int dailyPlaytime, LocalDate statsDate) {
        this.videoId = videoId;
        this.userId = userId;
        this.statsAmount = statsAmount;
        this.dailyViews = dailyViews;
        this.dailyAdViews = dailyAdViews;
        this.dailyPlaytime = dailyPlaytime;
        this.statsDate = statsDate;
    }
}