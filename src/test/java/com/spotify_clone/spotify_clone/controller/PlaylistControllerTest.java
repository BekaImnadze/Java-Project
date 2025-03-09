package com.spotify_clone.spotify_clone.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spotify_clone.spotify_clone.config.JwtUtil;
import com.spotify_clone.spotify_clone.dto.PlaylistDto;
import com.spotify_clone.spotify_clone.entities.Playlist;
import com.spotify_clone.spotify_clone.entities.User;
import com.spotify_clone.spotify_clone.service.PlaylistService;
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

@WebMvcTest(PlaylistController.class)
public class PlaylistControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PlaylistService playlistService;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtUtil jwtUtil;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCreatePlaylist() throws Exception {

        PlaylistDto playlistDto = new PlaylistDto();
        playlistDto.setName("Test Playlist");
        playlistDto.setMusicIds(Collections.singletonList(1L));

        User user = new User();
        user.setUsername("testuser");

        Playlist playlist = new Playlist();
        playlist.setName("Test Playlist");

        when(jwtUtil.getUsernameFromToken("jwt-token")).thenReturn("testuser");
        when(userService.findByUsername("testuser")).thenReturn(user);
        when(playlistService.createPlaylist(any(PlaylistDto.class), eq(user))).thenReturn(playlist);

        mockMvc.perform(post("/api/playlists")
                        .header("Authorization", "Bearer jwt-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(playlistDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Playlist"));
    }

    @Test
    public void testUpdatePlaylist() throws Exception {
        PlaylistDto playlistDto = new PlaylistDto();
        playlistDto.setName("Updated Playlist");
        playlistDto.setMusicIds(Collections.singletonList(2L));

        Playlist updatedPlaylist = new Playlist();
        updatedPlaylist.setName("Updated Playlist");

        when(playlistService.updatePlaylist(1L, any(PlaylistDto.class))).thenReturn(updatedPlaylist);

        mockMvc.perform(put("/api/playlists/1")
                        .header("Authorization", "Bearer jwt-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(playlistDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Playlist"));
    }
}