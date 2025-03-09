package com.spotify_clone.spotify_clone.repositories;

import com.spotify_clone.spotify_clone.entities.Genre;
import com.spotify_clone.spotify_clone.entities.Music;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MusicRepository extends JpaRepository<Music, Long> {
    List<Music> findByNameContainingIgnoreCaseOrAuthorContainingIgnoreCase(String name, String author);
    List<Music> findByAlbumArtistId(Long artistId);
    List<Music> findByGenre(Genre genre);
}
