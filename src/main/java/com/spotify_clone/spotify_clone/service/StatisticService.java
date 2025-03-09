package com.spotify_clone.spotify_clone.service;

import com.spotify_clone.spotify_clone.entities.ListenStatistic;
import com.spotify_clone.spotify_clone.repositories.ListenStatisticRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class StatisticService {

    @Autowired
    private ListenStatisticRepository listenStatisticRepository;

    public void generateStatistics() {
        LocalDate lastFriday = LocalDate.now().minusDays(LocalDate.now().getDayOfWeek().getValue() + 2); // Get the last Friday
        List<ListenStatistic> statistics = listenStatisticRepository.findByStatisticDate(lastFriday);
        System.out.println("Weekly Statistics for " + lastFriday + ":");
        statistics.forEach(statistic -> {
            System.out.println("Music ID: " + statistic.getMusic().getId() + ", Listens: " + statistic.getListenCount());
        });
    }
}
