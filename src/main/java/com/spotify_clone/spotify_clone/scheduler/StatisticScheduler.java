package com.spotify_clone.spotify_clone.scheduler;

import com.spotify_clone.spotify_clone.service.StatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class StatisticScheduler {

    @Autowired
    private StatisticService statisticService;

    @Scheduled(cron = "0 0 0 * * FRI") // Every Friday at midnight
    public void generateWeeklyStatistics() {
        statisticService.generateStatistics();
    }
}