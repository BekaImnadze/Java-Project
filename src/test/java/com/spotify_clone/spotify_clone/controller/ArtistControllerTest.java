package com.spotify_clone.spotify_clone.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spotify_clone.spotify_clone.config.JwtUtil;
import com.spotify_clone.spotify_clone.dto.AlbumDto;
import com.spotify_clone.spotify_clone.dto.MusicDto;
import com.spotify_clone.spotify_clone.entities.Album;
import com.spotify_clone.spotify_clone.entities.Music;
import com.spotify_clone.spotify_clone.entities.User;
import com.spotify_clone.spotify_clone.service.AlbumService;
import com.spotify_clone.spotify_clone.service.MusicService;
import com.spotify_clone.spotify_clone.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ArtistController.class)
public class ArtistControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MusicService musicService;

    @MockBean
    private AlbumService albumService;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtUtil jwtUtil;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCreateAlbum() throws Exception {
        AlbumDto albumDto = new AlbumDto();
        albumDto.setName("Test Album");

        User artist = new User();
        artist.setId(1L);

        Album album = new Album();
        album.setName("Test Album");

        when(jwtUtil.getUsernameFromToken("jwt-token")).thenReturn("testuser");
        when(userService.findByUsername("testuser")).thenReturn(artist);
        when(albumService.createAlbum(any(AlbumDto.class), eq(artist))).thenReturn(album);

        mockMvc.perform(post("/api/artists/albums")
                        .header("Authorization", "Bearer jwt-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(albumDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Album"));
    }

    @Test
    public void testUploadMusic() throws Exception {
        MusicDto musicDto = new MusicDto();
        musicDto.setName("New Song");

        Album album = new Album();
        album.setId(1L);

        Music music = new Music();
        music.setName("New Song");

        when(albumService.findById(1L)).thenReturn(album);
        when(musicService.uploadMusic(any(MusicDto.class), eq(album))).thenReturn(music);

        mockMvc.perform(post("/api/artists/music")
                        .param("albumId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(musicDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("New Song"));
    }

    @Test
    public void testGetSimilarArtists() throws Exception {
        Music music = new Music();
        music.setName("Similar Song");
        when(musicService.getSimilarArtists(1L)).thenReturn(Collections.singletonList(music));

        mockMvc.perform(get("/api/artists/1/similar")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Similar Song"));
    }
}