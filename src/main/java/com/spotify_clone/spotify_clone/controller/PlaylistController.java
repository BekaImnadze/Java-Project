package com.spotify_clone.spotify_clone.controller;

import com.spotify_clone.spotify_clone.config.JwtUtil;
import com.spotify_clone.spotify_clone.dto.PlaylistDto;
import com.spotify_clone.spotify_clone.entities.Playlist;
import com.spotify_clone.spotify_clone.entities.User;
import com.spotify_clone.spotify_clone.service.PlaylistService;
import com.spotify_clone.spotify_clone.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/playlists")
public class PlaylistController {

    @Autowired
    private PlaylistService playlistService;
    @Autowired
    private UserService userService;
    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping
    public ResponseEntity<Playlist> createPlaylist(@RequestBody PlaylistDto playlistDto, HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        String username = jwtUtil.getUsernameFromToken(token);
        User user = userService.findByUsername(username);
        return ResponseEntity.ok(playlistService.createPlaylist(playlistDto, user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Playlist> updatePlaylist(@PathVariable Long id, @RequestBody PlaylistDto playlistDto, HttpServletRequest request) {
        return ResponseEntity.ok(playlistService.updatePlaylist(id, playlistDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePlaylist(@PathVariable Long id, HttpServletRequest request) {
        playlistService.deletePlaylist(id);
        return ResponseEntity.ok(Map.of("message", "Playlist deleted successfully."));
    }
}
