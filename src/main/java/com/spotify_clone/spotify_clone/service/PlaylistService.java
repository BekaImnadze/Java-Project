package com.spotify_clone.spotify_clone.service;

import com.spotify_clone.spotify_clone.dto.PlaylistDto;
import com.spotify_clone.spotify_clone.entities.Music;
import com.spotify_clone.spotify_clone.entities.Playlist;
import com.spotify_clone.spotify_clone.entities.User;
import com.spotify_clone.spotify_clone.exception.PlaylistNotFoundException;
import com.spotify_clone.spotify_clone.repositories.MusicRepository;
import com.spotify_clone.spotify_clone.repositories.PlaylistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class PlaylistService {

    @Autowired
    private PlaylistRepository playlistRepository;
    @Autowired
    private MusicRepository musicRepository;

    public Playlist createPlaylist(PlaylistDto playlistDto, User owner) {
        Playlist playlist = new Playlist();
        playlist.setName(playlistDto.getName());
        playlist.setOwner(owner);
        Set<Music> musics = new HashSet<>();
        for (Long musicId : playlistDto.getMusicIds()) {
            Music music = musicRepository.findById(musicId).orElse(null);
            if (music != null) {
                musics.add(music);
            }
        }
        playlist.setMusics(musics);
        return playlistRepository.save(playlist);
    }

    public Playlist updatePlaylist(Long id, PlaylistDto playlistDto) {
        Playlist playlist = playlistRepository.findById(id).orElseThrow(() -> new PlaylistNotFoundException("Playlist not found"));
        playlist.setName(playlistDto.getName());
        Set<Music> musics = new HashSet<>();
        for (Long musicId : playlistDto.getMusicIds()) {
            Music music = musicRepository.findById(musicId).orElse(null);
            if (music != null) {
                musics.add(music);
            }
        }
        playlist.setMusics(musics);
        return playlistRepository.save(playlist);
    }

    public void deletePlaylist(Long id) {
        playlistRepository.deleteById(id);
    }
}
