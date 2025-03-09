package com.spotify_clone.spotify_clone.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spotify_clone.spotify_clone.dto.MusicDto;
import com.spotify_clone.spotify_clone.entities.Music;
import com.spotify_clone.spotify_clone.service.MusicService;
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

@WebMvcTest(MusicController.class)
public class MusicControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MusicService musicService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testSearchMusic() throws Exception {

        Music music = new Music();
        music.setName("Test Song");
        when(musicService.searchMusic("test")).thenReturn(Collections.singletonList(music));

        mockMvc.perform(get("/api/music/search")
                        .param("query", "test")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Test Song"));
    }

    @Test
    public void testUploadMusic() throws Exception {
        MusicDto musicDto = new MusicDto();
        musicDto.setName("New Song");
        musicDto.setAuthor("Test Artist");
        musicDto.setGenre("Rock");

        Music music = new Music();
        music.setName("New Song");

        when(musicService.uploadMusic(any(MusicDto.class), any())).thenReturn(music);

        mockMvc.perform(post("/api/music")
                        .param("albumId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(musicDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("New Song"));
    }
}