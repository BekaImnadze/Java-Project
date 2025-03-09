package com.spotify_clone.spotify_clone.Service;

import com.spotify_clone.spotify_clone.dto.PlaylistDto;
import com.spotify_clone.spotify_clone.entities.Music;
import com.spotify_clone.spotify_clone.entities.Playlist;
import com.spotify_clone.spotify_clone.entities.User;
import com.spotify_clone.spotify_clone.exception.PlaylistNotFoundException;
import com.spotify_clone.spotify_clone.repositories.MusicRepository;
import com.spotify_clone.spotify_clone.repositories.PlaylistRepository;
import com.spotify_clone.spotify_clone.service.PlaylistService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PlaylistServiceTest {

    @Mock
    private PlaylistRepository playlistRepository;
    @Mock
    private MusicRepository musicRepository;

    @InjectMocks
    private PlaylistService playlistService;

    private PlaylistDto playlistDto;
    private Playlist playlist;
    private User owner;
    private Music music;

    @BeforeEach
    void setUp() {
        playlistDto = new PlaylistDto();
        playlistDto.setName("Test Playlist");
        playlistDto.setMusicIds(List.of(1L));

        owner = new User();
        owner.setId(1L);
        owner.setUsername("testUser");

        music = new Music();
        music.setId(1L);
        music.setName("Test Music");

        playlist = new Playlist();
        playlist.setId(1L);
        playlist.setName("Test Playlist");
        playlist.setOwner(owner);
        playlist.setMusics(Set.of(music));
    }

    @Test
    void createPlaylist_ShouldCreatePlaylist() {
        when(musicRepository.findById(1L)).thenReturn(Optional.of(music));
        when(playlistRepository.save(any(Playlist.class))).thenReturn(playlist);

        Playlist createdPlaylist = playlistService.createPlaylist(playlistDto, owner);

        assertNotNull(createdPlaylist);
        assertEquals("Test Playlist", createdPlaylist.getName());
        assertEquals(owner, createdPlaylist.getOwner());
    }

    @Test
    void updatePlaylist_ShouldUpdatePlaylist() {
        PlaylistDto updatedDto = new PlaylistDto();
        updatedDto.setName("Updated Playlist");
        updatedDto.setMusicIds(List.of(1L));

        Playlist updatedPlaylist = new Playlist();
        updatedPlaylist.setId(1L);
        updatedPlaylist.setName("Updated Playlist");
        updatedPlaylist.setOwner(owner);
        updatedPlaylist.setMusics(Set.of(music));

        when(playlistRepository.findById(1L)).thenReturn(Optional.of(playlist));
        when(musicRepository.findById(1L)).thenReturn(Optional.of(music));
        when(playlistRepository.save(any(Playlist.class))).thenReturn(updatedPlaylist);

        Playlist result = playlistService.updatePlaylist(1L, updatedDto);

        assertNotNull(result);
        assertEquals("Updated Playlist", result.getName());
    }

    @Test
    void deletePlaylist_ShouldDeletePlaylist() {
        playlistService.deletePlaylist(1L);
        verify(playlistRepository, times(1)).deleteById(1L);
    }

    @Test
    void findById_ShouldReturnPlaylist() {
        when(playlistRepository.findById(1L)).thenReturn(Optional.of(playlist));

        Playlist result = playlistRepository.findById(1L).get();

        assertNotNull(result);
        assertEquals("Test Playlist", result.getName());
    }

    @Test
    void findById_ShouldThrowExceptionIfPlaylistNotFound() {
        when(playlistRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(PlaylistNotFoundException.class, () -> playlistRepository.findById(1L).get());
    }
}