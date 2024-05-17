package org.example.shortsaccount.controller;

import lombok.RequiredArgsConstructor;
import org.example.shortsaccount.domain.Statistics;
import org.example.shortsaccount.dto.StatsDTO;
import org.example.shortsaccount.dto.TopFiveVideoDTO;
import org.example.shortsaccount.dto.TopFiveVideoInterface;
import org.example.shortsaccount.service.StatisticsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class StatisticsController {
    private final StatisticsService statisticsService;
    /*
    1. MSA -> USER, VIDEO, STATS
    2. 일간, 주간, 월간 테이블 추가 (성능 개선)
    3. 컨트롤러 코드 줄이고 서비스 쪽 리팩토링 함수화해서 20라인 이내 v
    4. 하드코딩 값 상수 & enum 처리
    */
    @PostMapping("/api/stats/force")
    public void createStatsForce() {
        statisticsService.createAllStatistics();
    }

    @GetMapping("/api/stats/{id}")
    public ResponseEntity<List<StatsDTO>> getStatistics(@PathVariable int id) {
        List<Statistics> stats = statisticsService.findByVideoId(id);
        List<StatsDTO> response = new ArrayList<>();
        for (Statistics stat : stats) {
            response.add(statisticsService.createStatsInfo(stat));
        }
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/api/daily/views")
    public ResponseEntity<List<TopFiveVideoDTO>> getDailyTopFiveViews() {
        List<TopFiveVideoDTO> dailyTopViews = statisticsService.findDailyTop5Views();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(dailyTopViews);
    }

    @GetMapping("/api/daily/playtime")
    public ResponseEntity<List<TopFiveVideoDTO>> getDailyTopFivePlaytime() {
        List<TopFiveVideoDTO> dailyTopPlaytime = statisticsService.findDailyTop5Playtime();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(dailyTopPlaytime);
    }

    @GetMapping("/api/weekly/views")
    public ResponseEntity<List<TopFiveVideoInterface>> getWeeklyTopFiveViews() {
        List<TopFiveVideoInterface> weeklyTopViews = statisticsService.findWeeklyTop5Views();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(weeklyTopViews);
    }

    @GetMapping("/api/weekly/playtime")
    public ResponseEntity<List<TopFiveVideoInterface>> getWeeklyTopFivePlaytime() {
        List<TopFiveVideoInterface> weeklyTopPlaytime = statisticsService.findWeeklyTop5Playtime();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(weeklyTopPlaytime);
    }
    @GetMapping("/api/monthly/views")
    public ResponseEntity<List<TopFiveVideoInterface>> getMonthlyTopFiveViews() {
        List<TopFiveVideoInterface> monthlyTopViews = statisticsService.findMonthlyTop5Views();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(monthlyTopViews);
    }

    @GetMapping("/api/monthly/playtime")
    public ResponseEntity<List<TopFiveVideoInterface>> getMonthlyTopFivePlaytime() {
        List<TopFiveVideoInterface> monthlyTopPlaytime = statisticsService.findMonthlyTop5Playtime();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(monthlyTopPlaytime);
    }
}