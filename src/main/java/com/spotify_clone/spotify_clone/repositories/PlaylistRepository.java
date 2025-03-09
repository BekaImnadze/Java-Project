package com.spotify_clone.spotify_clone.repositories;

import com.spotify_clone.spotify_clone.entities.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
}
