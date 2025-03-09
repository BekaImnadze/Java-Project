package com.spotify_clone.spotify_clone.Service;

import com.spotify_clone.spotify_clone.entities.ListenStatistic;
import com.spotify_clone.spotify_clone.entities.Music;
import com.spotify_clone.spotify_clone.repositories.ListenStatisticRepository;
import com.spotify_clone.spotify_clone.service.StatisticService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StatisticServiceTest {

    @Mock
    private ListenStatisticRepository listenStatisticRepository;

    @InjectMocks
    private StatisticService statisticService;

    @Test
    void generateStatistics_ShouldPrintStatistics() {
        Music music = new Music();
        music.setId(1L);

        ListenStatistic statistic = new ListenStatistic();
        statistic.setMusic(music);
        statistic.setListenCount(10L);
        statistic.setStatisticDate(LocalDate.now().minusDays(LocalDate.now().getDayOfWeek().getValue() + 2));

        List<ListenStatistic> statistics = new ArrayList<>();
        statistics.add(statistic);

        when(listenStatisticRepository.findByStatisticDate(any(LocalDate.class))).thenReturn(statistics);

        statisticService.generateStatistics();

        verify(listenStatisticRepository, times(1)).findByStatisticDate(any(LocalDate.class));
    }
}
