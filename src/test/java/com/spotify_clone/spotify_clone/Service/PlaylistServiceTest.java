package com.spotify_clone.spotify_clone.Service;

import com.spotify_clone.spotify_clone.dto.PlaylistDto;
import com.spotify_clone.spotify_clone.entities.Music;
import com.spotify_clone.spotify_clone.entities.Playlist;
import com.spotify_clone.spotify_clone.entities.User;
import com.spotify_clone.spotify_clone.repositories.MusicRepository;
import com.spotify_clone.spotify_clone.repositories.PlaylistRepository;
import com.spotify_clone.spotify_clone.service.PlaylistService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PlaylistServiceTest {

    @InjectMocks
    private PlaylistService playlistService;

    @Mock
    private PlaylistRepository playlistRepository;

    @Mock
    private MusicRepository musicRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreatePlaylist() {
        // Arrange
        PlaylistDto playlistDto = new PlaylistDto();
        playlistDto.setName("Test Playlist");
        playlistDto.setMusicIds(Collections.singletonList(1L));

        User owner = new User();
        owner.setId(1L);

        Music music = new Music();
        music.setId(1L);
        when(musicRepository.findById(1L)).thenReturn(Optional.of(music));
        when(playlistRepository.save(any(Playlist.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Playlist playlist = playlistService.createPlaylist(playlistDto, owner);

        assertEquals("Test Playlist", playlist.getName());
        assertEquals(owner, playlist.getOwner());
        assertEquals(1, playlist.getMusics().size());
        assertTrue(playlist.getMusics().contains(music));
    }

    @Test
    public void testUpdatePlaylist() {
        PlaylistDto playlistDto = new PlaylistDto();
        playlistDto.setName("Updated Playlist");
        playlistDto.setMusicIds(Collections.singletonList(2L));

        Playlist existingPlaylist = new Playlist();
        existingPlaylist.setId(1L);
        when(playlistRepository.findById(1L)).thenReturn(Optional.of(existingPlaylist));

        Music music = new Music();
        music.setId(2L);
        when(musicRepository.findById(2L)).thenReturn(Optional.of(music));
        when(playlistRepository.save(any(Playlist.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Playlist updatedPlaylist = playlistService.updatePlaylist(1L, playlistDto);

        assertEquals("Updated Playlist", updatedPlaylist.getName());
        assertEquals(1, updatedPlaylist.getMusics().size());
        assertTrue(updatedPlaylist.getMusics().contains(music));
    }
}