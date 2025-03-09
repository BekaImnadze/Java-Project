package com.spotify_clone.spotify_clone.controller;

import com.spotify_clone.spotify_clone.config.JwtUtil;
import com.spotify_clone.spotify_clone.entities.Music;
import com.spotify_clone.spotify_clone.entities.Playlist;
import com.spotify_clone.spotify_clone.entities.User;
import com.spotify_clone.spotify_clone.service.MusicService;
import com.spotify_clone.spotify_clone.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/music")
public class MusicController {

    @Autowired
    private MusicService musicService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserService userService;

    @GetMapping("/search")
    public ResponseEntity<List<Music>> searchMusic(@RequestParam String query) {
        return ResponseEntity.ok(musicService.searchMusic(query));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Music> getMusic(@PathVariable Long id, HttpServletRequest request) {
        return ResponseEntity.ok(musicService.findById(id));
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<?> downloadMusic(@PathVariable Long id, HttpServletRequest request) {
        musicService.recordListen(id);
        return ResponseEntity.ok(Map.of("message", "Music downloaded and listen recorded."));
    }

    @GetMapping("/sessionPlaylists")
    public ResponseEntity<List<Playlist>> getSessionPlaylists(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        String username = jwtUtil.getUsernameFromToken(token);
        User user = userService.findByUsername(username);
        return ResponseEntity.ok(musicService.generateSessionPlaylists(user.getId()));
    }
}
