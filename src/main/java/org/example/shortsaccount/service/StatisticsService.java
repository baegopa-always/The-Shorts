package org.example.shortsaccount.service;

import lombok.RequiredArgsConstructor;
import org.example.shortsaccount.domain.PlayHistory;
import org.example.shortsaccount.domain.Statistics;
import org.example.shortsaccount.domain.Video;
import org.example.shortsaccount.domain.VideoAdvertisement;
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

    // 일일 조회수
    // select count (*) from
    // play history 에서 video_id & 날짜로 묶은 다음에
    // last_watch_time 합치면 일일 영상 재생 시간
    // 여기서 count 하면 일일 조회수

    // 일일 광고 조회수
    // video_ad 에서 video_id & 날짜로 묶고
    // count

    // 위에 애들 모아서 amount 만들기
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
            TopFiveVideoDTO video = new TopFiveVideoDTO();
            video.setVideoId(stat.getVideoId());
            video.setViews(stat.getDailyViews());
            video.setPlaytime(stat.getDailyPlaytime());
            topVideos.add(video);
        }
        return topVideos;
    }

    //////////////////////////////////////////////////
    public List<TopFiveVideoInterface> findWeeklyTop5Views() {
        // 주간 뷰의 합산으로 해야..
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
        StatsDTO stats = new StatsDTO();
        stats.setVideoId(statistics.getVideoId());
        stats.setStatsAmount(statistics.getStatsAmount());
        stats.setDailyViewAmount(getViewsAmount(statistics.getDailyViews()));
        stats.setDailyAdAmount(getAdAmount(statistics.getDailyAdViews()));
        stats.setStatsDate(statistics.getStatsDate());
        return stats;
    }


    @Transactional
    public List<Statistics> createStatistics() {
        // 1. 영상 엔티티로 전부 가져오기
        List<Video> videos = videoRepository.findAll();
        // # 날짜 수정 필요
        // 매일 전날 영상들 통계 처리 하도록,,
        // # 성능 개선
        // 1. 통계 처리 끝난 play_history 제거 해야함 -> 재생 시간이 비디오 영상 길이랑 같으면 삭제하는 스케줄링..
        // 2. 인덱싱 활용 ??
        LocalDateTime to = LocalDateTime.now();
        LocalDateTime from = to.minusDays(1);
        LocalDate statsDate = LocalDate.now().minusDays(1);
        for (Video video : videos) {
            List<PlayHistory> videoHistory;
            // 2. 각 영상 id에 해당하는 모든 재생 기록 엔티티로 가져오기
            // 없으면 continue
            try {
            videoHistory = playHistoryRepository.findAllByVideoIdAndPlayDateBetween(
                    video.getVideoId(),
                    from,
                    to)
                    .orElseThrow(() -> new IllegalArgumentException("not found video: "));
            } catch (IllegalArgumentException e) {
                continue;
            }
            // 3. 일간 전체 재생 시간
            int dailyPlaytime = getDailyPlaytime(videoHistory);
            // 4. 일일 조회 수
            int dailyViews = videoHistory.size();
            List<VideoAdvertisement> videoAds = videoAdvertisementRepository.findAllByVideoIdAndAdTimestampBetween(
                    video.getVideoId(),
                    from,
                    to)
                    .orElseThrow(() -> new IllegalArgumentException("not found video: "));
            // 5. 일일 광고 조회 수
            int dailyAdViews = videoAds.size();
            // 6. 조회수, 광고 조회수로 일간 정산
            int amount = getAmount(dailyViews, dailyAdViews);
            // 7. 영상 id에 대하여 날짜 설정하고 통계 정산 저장
            statisticsRepository.save(Statistics.builder()
                    .videoId(video.getVideoId())
                    .statsAmount(amount)
                    .dailyViews(dailyViews)
                    .dailyAdViews(dailyAdViews)
                    .dailyPlaytime(dailyPlaytime)
                    .statsDate(statsDate)
                    .userId(video.getMemberId())
                    .build());
        }
        return statisticsRepository.findAll();
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
