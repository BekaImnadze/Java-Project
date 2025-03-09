package com.spotify_clone.spotify_clone.service;

import com.spotify_clone.spotify_clone.dto.MusicDto;
import com.spotify_clone.spotify_clone.entities.*;
import com.spotify_clone.spotify_clone.exception.MusicNotFoundException;
import com.spotify_clone.spotify_clone.repositories.GenreRepository;
import com.spotify_clone.spotify_clone.repositories.ListenStatisticRepository;
import com.spotify_clone.spotify_clone.repositories.MusicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MusicService {

    @Autowired
    private MusicRepository musicRepository;
    @Autowired
    private GenreRepository genreRepository;
    @Autowired
    private ListenStatisticRepository listenStatisticRepository;

    public Music uploadMusic(MusicDto musicDto, Album album) {
        Music music = new Music();
        music.setName(musicDto.getName());
        music.setAuthor(musicDto.getAuthor());
        music.setAlbum(album);
        Genre genre = genreRepository.findByName(musicDto.getGenre());
        music.setGenre(genre);
        return musicRepository.save(music);
    }

    public Music updateMusic(Long id, MusicDto musicDto) {
        Music music = musicRepository.findById(id).orElseThrow(() -> new MusicNotFoundException("Music not found"));
        music.setName(musicDto.getName());
        music.setAuthor(musicDto.getAuthor());
        Genre genre = genreRepository.findByName(musicDto.getGenre());
        music.setGenre(genre);

        return musicRepository.save(music);
    }

    public void deleteMusic(Long id) {
        musicRepository.deleteById(id);
    }

    public List<Music> searchMusic(String query) {
        return musicRepository.findByNameContainingIgnoreCaseOrAuthorContainingIgnoreCase(query, query);
    }

    public void recordListen(Long musicId) {
        Music music = musicRepository.findById(musicId).orElseThrow(() -> new MusicNotFoundException("Music not found"));
        ListenStatistic statistic = listenStatisticRepository.findByMusicAndStatisticDate(music, LocalDate.now())
                .orElse(new ListenStatistic());
        statistic.setMusic(music);
        statistic.setListenCount(statistic.getListenCount() == null ? 1 : statistic.getListenCount() + 1);
        statistic.setStatisticDate(LocalDate.now());
        listenStatisticRepository.save(statistic);
    }

    public List<Music> getSimilarArtists(Long artistId) {
        User artist = musicRepository.findByAlbumArtistId(artistId).get(0).getAlbum().getArtist();
        List<Music> artistMusics = musicRepository.findByAlbumArtistId(artistId);
        if(artistMusics.isEmpty()){
            return new ArrayList<>();
        }
        Genre artistGenre = artistMusics.get(0).getGenre();
        List<Music> similarArtistsMusics = musicRepository.findByGenre(artistGenre);
        return similarArtistsMusics.stream().filter(m -> !m.getAlbum().getArtist().getId().equals(artistId)).limit(10).collect(Collectors.toList());
    }

    public List<Playlist> generateSessionPlaylists(Long userId) {
        List<ListenStatistic> userStatistics = listenStatisticRepository.findByMusicAlbumArtistId(userId);
        if (userStatistics.isEmpty()) {
            return new ArrayList<>();
        }

        List<Genre> topGenres = userStatistics.stream()
                .collect(Collectors.groupingBy(statistic -> statistic.getMusic().getGenre(), Collectors.counting()))
                .entrySet().stream()
                .sorted(java.util.Map.Entry.<Genre, Long>comparingByValue().reversed())
                .limit(3)
                .map(java.util.Map.Entry::getKey)
                .collect(Collectors.toList());

        List<Playlist> sessionPlaylists = new ArrayList<>();
        for (Genre genre : topGenres) {
            Playlist playlist = new Playlist();
            playlist.setName(genre.getName() + " Session");
            playlist.setOwner(musicRepository.findByAlbumArtistId(userId).get(0).getAlbum().getArtist());
            List<Music> genreMusics = musicRepository.findByGenre(genre);
            playlist.setMusics(genreMusics.stream().limit(20).collect(Collectors.toSet()));
            sessionPlaylists.add(playlist);
        }
        return sessionPlaylists;
    }

    public Music findById(Long id) {
        return musicRepository.findById(id).orElseThrow(() -> new MusicNotFoundException("Music not found"));
    }
}
