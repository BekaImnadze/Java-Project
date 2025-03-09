package com.spotify_clone.spotify_clone.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spotify_clone.spotify_clone.dto.UserDto;
import com.spotify_clone.spotify_clone.entities.User;
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

@WebMvcTest(AdminController.class)
public class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetUsers() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        when(userService.findAll()).thenReturn(Collections.singletonList(user));

        mockMvc.perform(get("/api/admin/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("testuser"));
    }

    @Test
    public void testUpdateUser() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setUsername("updateduser");
        userDto.setEmail("updated@example.com");

        User updatedUser = new User();
        updatedUser.setId(1L);
        updatedUser.setUsername("updateduser");
        when(userService.updateUser(1L, userDto)).thenReturn(updatedUser);

        mockMvc.perform(put("/api/admin/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("updateduser"));
    }

    @Test
    public void testDeleteUser() throws Exception {
        doNothing().when(userService).deleteUser(1L);

        mockMvc.perform(delete("/api/admin/users/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User deleted successfully."));
    }

    @Test
    public void testBlockUser() throws Exception {
        doNothing().when(userService).blockUser(1L);

        mockMvc.perform(put("/api/admin/users/1/block")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User blocked successfully."));
    }
}