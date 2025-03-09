package com.spotify_clone.spotify_clone.controller;

import com.spotify_clone.spotify_clone.config.JwtUtil;
import com.spotify_clone.spotify_clone.dto.AlbumDto;
import com.spotify_clone.spotify_clone.dto.MusicDto;
import com.spotify_clone.spotify_clone.entities.Album;
import com.spotify_clone.spotify_clone.entities.Music;
import com.spotify_clone.spotify_clone.entities.User;
import com.spotify_clone.spotify_clone.service.AlbumService;
import com.spotify_clone.spotify_clone.service.MusicService;
import com.spotify_clone.spotify_clone.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/artists")
public class ArtistController {

    private final MusicService musicService;
    private final AlbumService albumService;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    public ArtistController(MusicService musicService, AlbumService albumService, UserService userService, JwtUtil jwtUtil) {
        this.musicService = musicService;
        this.albumService = albumService;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/albums")
    public ResponseEntity<Album> createAlbum(@RequestBody AlbumDto albumDto, HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body(null);
        }
        String token = authHeader.substring(7);
        String username = jwtUtil.getUsernameFromToken(token);
        User artist = userService.findByUsername(username);
        return ResponseEntity.ok(albumService.createAlbum(albumDto, artist));
    }

    @PostMapping("/music")
    public ResponseEntity<Music> uploadMusic(@RequestBody MusicDto musicDto, @RequestParam Long albumId, HttpServletRequest request) {
        Album album = albumService.findById(albumId);
        return ResponseEntity.ok(musicService.uploadMusic(musicDto, album));
    }

    @GetMapping("/{artistId}/similar")
    public ResponseEntity<List<Music>> getSimilarArtists(@PathVariable Long artistId) {
        return ResponseEntity.ok(musicService.getSimilarArtists(artistId));
    }
}

