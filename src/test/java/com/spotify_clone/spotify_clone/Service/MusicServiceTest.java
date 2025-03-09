package com.spotify_clone.spotify_clone.Service;

import com.spotify_clone.spotify_clone.dto.MusicDto;
import com.spotify_clone.spotify_clone.entities.*;
import com.spotify_clone.spotify_clone.exception.MusicNotFoundException;
import com.spotify_clone.spotify_clone.repositories.GenreRepository;
import com.spotify_clone.spotify_clone.repositories.ListenStatisticRepository;
import com.spotify_clone.spotify_clone.repositories.MusicRepository;
import com.spotify_clone.spotify_clone.service.MusicService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MusicServiceTest {

    @Mock
    private MusicRepository musicRepository;
    @Mock
    private GenreRepository genreRepository;
    @Mock
    private ListenStatisticRepository listenStatisticRepository;

    @InjectMocks
    private MusicService musicService;

    private MusicDto musicDto;
    private Music music;
    private Album album;
    private Genre genre;

    @BeforeEach
    void setUp() {
        musicDto = new MusicDto();
        musicDto.setName("Test Music");
        musicDto.setAuthor("Test Author");
        musicDto.setGenre("Pop");

        album = new Album();
        album.setId(1L);
        album.setName("Test Album");

        genre = new Genre();
        genre.setId(1L);
        genre.setName("Pop");

        music = new Music();
        music.setId(1L);
        music.setName("Test Music");
        music.setAuthor("Test Author");
        music.setAlbum(album);
        music.setGenre(genre);
    }

    @Test
    void uploadMusic_ShouldUploadMusic() {
        when(genreRepository.findByName("Pop")).thenReturn(genre);
        when(musicRepository.save(any(Music.class))).thenReturn(music);

        Music uploadedMusic = musicService.uploadMusic(musicDto, album);

        assertNotNull(uploadedMusic);
        assertEquals("Test Music", uploadedMusic.getName());
        assertEquals(genre, uploadedMusic.getGenre());
    }

    @Test
    void updateMusic_ShouldUpdateMusic() {
        MusicDto updatedDto = new MusicDto();
        updatedDto.setName("Updated Music");
        updatedDto.setAuthor("Updated Author");
        updatedDto.setGenre("Rock");

        Genre rockGenre = new Genre();
        rockGenre.setId(2L);
        rockGenre.setName("Rock");

        Music updatedMusic = new Music();
        updatedMusic.setId(1L);
        updatedMusic.setName("Updated Music");
        updatedMusic.setAuthor("Updated Author");
        updatedMusic.setAlbum(album);
        updatedMusic.setGenre(rockGenre);

        when(musicRepository.findById(1L)).thenReturn(Optional.of(music));
        when(genreRepository.findByName("Rock")).thenReturn(rockGenre);
        when(musicRepository.save(any(Music.class))).thenReturn(updatedMusic);

        Music result = musicService.updateMusic(1L, updatedDto);

        assertNotNull(result);
        assertEquals("Updated Music", result.getName());
        assertEquals(rockGenre, result.getGenre());
    }

    @Test
    void deleteMusic_ShouldDeleteMusic() {
        musicService.deleteMusic(1L);
        verify(musicRepository, times(1)).deleteById(1L);
    }

    @Test
    void searchMusic_ShouldReturnListOfMusic() {
        List<Music> musics = new ArrayList<>();
        musics.add(music);
        when(musicRepository.findByNameContainingIgnoreCaseOrAuthorContainingIgnoreCase("test", "test")).thenReturn(musics);

        List<Music> result = musicService.searchMusic("test");

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    void recordListen_ShouldRecordListen() {
        ListenStatistic statistic = new ListenStatistic();
        statistic.setMusic(music);
        statistic.setListenCount(1L);
        statistic.setStatisticDate(LocalDate.now());

        when(musicRepository.findById(1L)).thenReturn(Optional.of(music));
        when(listenStatisticRepository.findByMusicAndStatisticDate(music, LocalDate.now())).thenReturn(Optional.of(statistic));
        when(listenStatisticRepository.save(any(ListenStatistic.class))).thenReturn(statistic);

        musicService.recordListen(1L);

        verify(listenStatisticRepository, times(1)).save(any(ListenStatistic.class));
    }

    @Test
    void getSimilarArtists_ShouldReturnListOfMusic() {
        User artist = new User();
        artist.setId(1L);

        Album artistAlbum = new Album();
        artistAlbum.setId(1L);
        artistAlbum.setArtist(artist);

        Music artistMusic = new Music();
        artistMusic.setId(1L);
        artistMusic.setAlbum(artistAlbum);
        artistMusic.setGenre(genre);

        List<Music> artistMusics = new ArrayList<>();
        artistMusics.add(artistMusic);

        Music similarMusic = new Music();
        similarMusic.setId(2L);
        similarMusic.setGenre(genre);

        List<Music> similarMusics = new ArrayList<>();
        similarMusics.add(similarMusic);

        when(musicRepository.findByAlbumArtistId(1L)).thenReturn(artistMusics);
        when(musicRepository.findByGenre(genre)).thenReturn(similarMusics);

        List<Music> result = musicService.getSimilarArtists(1L);

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    void generateSessionPlaylists_ShouldReturnListOfPlaylists() {
        User artist = new User();
        artist.setId(1L);

        Album artistAlbum = new Album();
        artistAlbum.setId(1L);
        artistAlbum.setArtist(artist);

        Music artistMusic = new Music();
        artistMusic.setId(1L);
        artistMusic.setAlbum(artistAlbum);
        artistMusic.setGenre(genre);

        ListenStatistic statistic = new ListenStatistic();
        statistic.setMusic(artistMusic);

        List<ListenStatistic> statistics = new ArrayList<>();
        statistics.add(statistic);

        List<Music> genreMusics = new ArrayList<>();
        genreMusics.add(artistMusic);
        when(listenStatisticRepository.

        findByMusicAlbumArtistId(1L)).thenReturn(statistics);
        when(musicRepository.findByAlbumArtistId(1L)).thenReturn(List.of(artistMusic));
        when(musicRepository.findByGenre(genre)).thenReturn(genreMusics);

        List<Playlist> result = musicService.generateSessionPlaylists(1L);

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    void findById_ShouldReturnMusic() {
        when(musicRepository.findById(1L)).thenReturn(Optional.of(music));

        Music result = musicService.findById(1L);

        assertNotNull(result);
        assertEquals("Test Music", result.getName());
    }

    @Test
    void findById_ShouldThrowExceptionIfMusicNotFound() {
        when(musicRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(MusicNotFoundException.class, () -> musicService.findById(1L));
    }
}
