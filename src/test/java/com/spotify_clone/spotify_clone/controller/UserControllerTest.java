package com.spotify_clone.spotify_clone.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spotify_clone.spotify_clone.dto.UserDto;
import com.spotify_clone.spotify_clone.entities.User;
import com.spotify_clone.spotify_clone.enums.UserRole;
import com.spotify_clone.spotify_clone.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    private UserDto userDto;
    private User user;

    @BeforeEach
    void setUp() {
        userDto = new UserDto();
        userDto.setUsername("testUser");
        userDto.setPassword("password");
        userDto.setEmail("test@example.com");

        user = new User();
        user.setId(1L);
        user.setUsername("testUser");
        user.setPassword("encodedPassword");
        user.setEmail("test@example.com");
    }

    @Test
    void register_ShouldReturnOkAndMessage() throws Exception {
        when(userService.register(any(UserDto.class), any(UserRole.class))).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto))
                        .param("role", "LISTENER"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "<span class=""));
}
    @Test
    void verify\_ShouldReturnOkAndMessage\(\) throws Exception \{
        mockMvc\.perform\(MockMvcRequestBuilders\.post\("/api/users/verify"\)
\.param\("email", "test@example\.com"\)
\.param\("code", "123456"\)\)
\.andExpect\(MockMvcResultMatchers\.status\(\)\.isOk\(\)\)
\.andExpect\(MockMvcResultMatchers\.jsonPath\("</span>.message").value("Email verified successfully."));
    }

    @Test
    void login_ShouldReturnOkAndToken() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("<span class="math-inline">\.token"\)\.exists\(\)\);</9\>
\}
    @Test
    void profile\_ShouldReturnOkAndUser\(\) throws Exception \{
        when\(userService\.findByUsername\("testUser"\)\)\.thenReturn\(user\);
        mockMvc\.perform\(MockMvcRequestBuilders\.get\("/api/users/profile"\)
\.header\("Authorization", "Bearer testToken"\)\)
\.andExpect\(MockMvcResultMatchers\.status\(\)\.isOk\(\)\)
\.andExpect\(MockMvcResultMatchers\.jsonPath\("</span>.username").value("testUser"));
    }
}

