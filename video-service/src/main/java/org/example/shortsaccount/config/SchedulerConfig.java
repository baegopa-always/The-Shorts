package org.example.shortsaccount.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.shortsaccount.service.StatisticsService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SchedulerConfig {
    private final StatisticsService statisticsService;
    @Scheduled(cron = "0 0 1 * * ?")
    public void createAllStats() {
        try {
            statisticsService.createAllStatistics();
            log.info("scheduled success");
        } catch (Exception e) {
            log.info("scheduled error");
        }
    }
}