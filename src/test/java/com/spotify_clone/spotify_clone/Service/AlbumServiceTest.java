package com.spotify_clone.spotify_clone.Service;

import com.spotify_clone.spotify_clone.dto.AlbumDto;
import com.spotify_clone.spotify_clone.entities.Album;
import com.spotify_clone.spotify_clone.entities.User;
import com.spotify_clone.spotify_clone.exception.AlbumNotFoundException;
import com.spotify_clone.spotify_clone.repositories.AlbumRepository;
import com.spotify_clone.spotify_clone.service.AlbumService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static jdk.internal.org.objectweb.asm.util.CheckClassAdapter.verify;
import static org.hamcrest.Matchers.any;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AlbumServiceTest {

    @Mock
    private AlbumRepository albumRepository;

    @InjectMocks
    private AlbumService albumService;

    private AlbumDto albumDto;
    private Album album;
    private User artist;

    @BeforeEach
    void setUp() {
        albumDto = new AlbumDto();
        albumDto.setName("Test Album");

        artist = new User();
        artist.setId(1L);
        artist.setUsername
        ("testArtist");

        album = new Album();
        album.setId(1L);
        album.setName("Test Album");
        album.setArtist(artist);
    }

    @Test
    void createAlbum_ShouldCreateAlbum() {
        when(albumRepository.save(any(Album.class))).thenReturn(album);

        Album createdAlbum = albumService.createAlbum(albumDto, artist);

        assertNotNull(createdAlbum);
        assertEquals("Test Album", createdAlbum.getName());
        assertEquals(artist, createdAlbum.getArtist());
    }

    @Test
    void updateAlbum_ShouldUpdateAlbum() {
        AlbumDto updatedDto = new AlbumDto();
        updatedDto.setName("Updated Album");

        Album updatedAlbum = new Album();
        updatedAlbum.setId(1L);
        updatedAlbum.setName("Updated Album");
        updatedAlbum.setArtist(artist);

        when(albumRepository.findById(1L)).thenReturn(Optional.of(album));
        when(albumRepository.save(any(Album.class))).thenReturn(updatedAlbum);

        Album result = albumService.updateAlbum(1L, updatedDto);

        assertNotNull(result);
        assertEquals("Updated Album", result.getName());
    }

    @Test
    void deleteAlbum_ShouldDeleteAlbum() {
        albumService.deleteAlbum(1L);
        verify(albumRepository, times(1)).deleteById(1L);
    }

    @Test
    void findById_ShouldReturnAlbum() {
        when(albumRepository.findById(1L)).thenReturn(Optional.of(album));

        Album result = albumService.findById(1L);

        assertNotNull(result);
        assertEquals("Test Album", result.getName());
    }

    @Test
    void findById_ShouldThrowExceptionIfAlbumNotFound() {
        when(albumRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(AlbumNotFoundException.class, () -> albumService.findById(1L));
    }
}
