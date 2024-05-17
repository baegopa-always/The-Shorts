package org.example.shortsaccount.service;

import lombok.RequiredArgsConstructor;
import org.example.shortsaccount.domain.PlayHistory;
import org.example.shortsaccount.domain.Statistics;
import org.example.shortsaccount.domain.Video;
import org.example.shortsaccount.dto.StatsDTO;
import org.example.shortsaccount.dto.TopFiveVideoDTO;
import org.example.shortsaccount.dto.TopFiveVideoInterface;
import org.example.shortsaccount.repository.PlayHistoryRepository;
import org.example.shortsaccount.repository.StatisticsRepository;
import org.example.shortsaccount.repository.VideoAdvertisementRepository;
import org.example.shortsaccount.repository.VideoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class StatisticsService {
    private final StatisticsRepository statisticsRepository;
    private final PlayHistoryRepository playHistoryRepository;
    private final VideoRepository videoRepository;
    private final VideoAdvertisementRepository videoAdvertisementRepository;

    public List<TopFiveVideoDTO> findDailyTop5Views() {
        List<Statistics> dailyTop5Views = statisticsRepository.findDailyTop5Views(LocalDate.now().minusDays(1));
        return topVideos(dailyTop5Views);
    }

    public List<TopFiveVideoDTO> findDailyTop5Playtime() {
        List<Statistics> dailyTop5Playtime = statisticsRepository.findDailyTop5Playtime(LocalDate.now().minusDays(1));
        return topVideos(dailyTop5Playtime);
    }

    public List<TopFiveVideoDTO> topVideos(List<Statistics> dailyTop5) {
        List<TopFiveVideoDTO> topVideos = new ArrayList<>();
        for (Statistics stat : dailyTop5) {
            topVideos.add(TopFiveVideoDTO.builder()
                    .videoId(stat.getVideoId())
                    .views(stat.getDailyViews())
                    .playtime(stat.getDailyPlaytime())
                    .build());
        }
        return topVideos;
    }

    public List<TopFiveVideoInterface> findWeeklyTop5Views() {
        return statisticsRepository.findTop5ViewsBetween(LocalDate.now().minusWeeks(1),LocalDate.now());
    }

    public List<TopFiveVideoInterface> findWeeklyTop5Playtime() {
        return statisticsRepository.findTop5PlaytimeBetween(LocalDate.now().minusWeeks(1),LocalDate.now());
    }

    public List<TopFiveVideoInterface> findMonthlyTop5Views() {
        return statisticsRepository.findTop5ViewsBetween(LocalDate.now().minusMonths(1),LocalDate.now());
    }

    public List<TopFiveVideoInterface> findMonthlyTop5Playtime() {
        return statisticsRepository.findTop5PlaytimeBetween(LocalDate.now().minusMonths(1),LocalDate.now());
    }

    public List<Statistics> findByVideoId(int id) {
        return statisticsRepository.findAllByVideoId(id)
                .orElseThrow(() -> new IllegalArgumentException("not found video: " + id));
    }

    public StatsDTO createStatsInfo(Statistics statistics) {
        return StatsDTO.builder()
                .videoId(statistics.getVideoId())
                .statsAmount(statistics.getStatsAmount())
                .dailyViewAmount(getViewsAmount(statistics.getDailyViews()))
                .dailyAdAmount(getAdAmount(statistics.getDailyAdViews()))
                .statsDate(statistics.getStatsDate())
                .build();
    }

    @Transactional
    public void createAllStatistics() {
        List<Video> videos = videoRepository.findAll();
        LocalDate now = LocalDate.now();
        for (Video video : videos) {
            createStats(video, now.atStartOfDay().minusDays(1), now.atStartOfDay(), now);
        }
    }

    private void createStats(Video video, LocalDateTime from, LocalDateTime to, LocalDate now) {
        try {
            List<PlayHistory> videoHistory = playHistoryRepository.findAllByVideoIdAndPlayDateBetween(
                            video.getVideoId(),
                            from,
                            to)
                    .orElseThrow(() -> new IllegalArgumentException("not found video: "));
            save(video,
                videoHistory.size(),
                videoAdvertisementRepository.findAllByVideoIdAndAdTimestampBetween(
                                video.getVideoId(),
                                from,
                                to)
                        .orElseThrow(() -> new IllegalArgumentException("not found video: ")).size(),
                getDailyPlaytime(videoHistory),
                now);
        } catch (IllegalArgumentException e) {
            return;
        }
    }

    private void save(Video video, int dailyViews, int dailyAdViews, int dailyPlaytime, LocalDate statsDate) {
        statisticsRepository.save(Statistics.builder()
                .videoId(video.getVideoId())
                .statsAmount(getAmount(dailyViews, dailyAdViews))
                .dailyViews(dailyViews)
                .dailyAdViews(dailyAdViews)
                .dailyPlaytime(dailyPlaytime)
                .statsDate(statsDate)
                .userId(video.getMemberId())
                .build());
    }

    private int getDailyPlaytime(List<PlayHistory> videoHistory) {
        return videoHistory.stream().mapToInt(PlayHistory::getLastWatchTime).sum();
    }

    private int getAmount(int dailyViews, int dailyAdViews) {
        return getViewsAmount(dailyViews) + getAdAmount(dailyAdViews);
    }

    private int getViewsAmount(int dailyViews) {
        int sum = 0;
        if (dailyViews >= 1_000_000) {
            sum += (int)((dailyViews - 999_999) * 1.5);
            dailyViews = 999_999;
        }
        if (dailyViews >= 500_000) {
            sum += (int)((dailyViews - 499_999) * 1.3);
            dailyViews = 499_999;
        }
        if (dailyViews >= 100_000) {
            sum += (int)((dailyViews - 99_999) * 1.1);
            dailyViews = 99_999;
        }
        sum += dailyViews;
        return sum;
    }

    private int getAdAmount(int dailyAdViews) {
        int sum = 0;
        if (dailyAdViews >= 1_000_000) {
            sum += (dailyAdViews - 999_999) * 20;
            dailyAdViews = 999_999;
        }
        if (dailyAdViews >= 500_000) {
            sum += (dailyAdViews - 499_999) * 15;
            dailyAdViews = 499_999;
        }
        if (dailyAdViews >= 100_000) {
            sum += (dailyAdViews - 99_999) * 12;
            dailyAdViews = 99_999;
        }
        sum += dailyAdViews * 10;
        return sum;
    }
}
