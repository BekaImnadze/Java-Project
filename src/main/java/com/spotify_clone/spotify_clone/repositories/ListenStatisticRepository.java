package com.spotify_clone.spotify_clone.repositories;

import com.spotify_clone.spotify_clone.entities.ListenStatistic;
import com.spotify_clone.spotify_clone.entities.Music;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ListenStatisticRepository extends JpaRepository<ListenStatistic, Long> {
    Optional<ListenStatistic> findByMusicAndStatisticDate(Music music, LocalDate date);
    List<ListenStatistic> findByStatisticDate(LocalDate date);
    List<ListenStatistic> findByMusic_Album_ArtistId(Long artistId);
}