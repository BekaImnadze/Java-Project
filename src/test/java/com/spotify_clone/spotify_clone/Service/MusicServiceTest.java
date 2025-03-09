package com.spotify_clone.spotify_clone.Service;

import com.spotify_clone.spotify_clone.dto.MusicDto;
import com.spotify_clone.spotify_clone.entities.Album;
import com.spotify_clone.spotify_clone.entities.Genre;
import com.spotify_clone.spotify_clone.entities.Music;
import com.spotify_clone.spotify_clone.repositories.GenreRepository;
import com.spotify_clone.spotify_clone.repositories.ListenStatisticRepository;
import com.spotify_clone.spotify_clone.repositories.MusicRepository;
import com.spotify_clone.spotify_clone.service.MusicService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MusicServiceTest {

    @InjectMocks
    private MusicService musicService;

    @Mock
    private MusicRepository musicRepository;

    @Mock
    private GenreRepository genreRepository;

    @Mock
    private ListenStatisticRepository listenStatisticRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testUploadMusic() {
        MusicDto musicDto = new MusicDto();
        musicDto.setName("Test Song");
        musicDto.setAuthor("Test Artist");
        musicDto.setGenre("Rock");

        Album album = new Album();
        album.setId(1L);

        Genre genre = new Genre();
        genre.setName("Rock");
        when(genreRepository.findByName("Rock")).thenReturn(genre);
        when(musicRepository.save(any(Music.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Music music = musicService.uploadMusic(musicDto, album);

        assertEquals("Test Song", music.getName());
        assertEquals("Test Artist", music.getAuthor());
        assertEquals(album, music.getAlbum());
        assertEquals(genre, music.getGenre());
    }

    @Test
    public void testSearchMusic() {
        Music music = new Music();
        music.setName("Test Song");
        when(musicRepository.findByNameContainingIgnoreCaseOrAuthorContainingIgnoreCase("test", "test"))
                .thenReturn(Collections.singletonList(music));

        var result = musicService.searchMusic("test");

        assertEquals(1, result.size());
        assertEquals("Test Song", result.get(0).getName());
    }
}