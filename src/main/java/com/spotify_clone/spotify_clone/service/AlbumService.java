package com.spotify_clone.spotify_clone.service;

import com.spotify_clone.spotify_clone.dto.AlbumDto;
import com.spotify_clone.spotify_clone.entities.Album;
import com.spotify_clone.spotify_clone.entities.User;
import com.spotify_clone.spotify_clone.exception.AlbumNotFoundException;
import com.spotify_clone.spotify_clone.repositories.AlbumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AlbumService {

    @Autowired
    private AlbumRepository albumRepository;

    public Album createAlbum(AlbumDto albumDto, User artist) {
        Album album = new Album();
        album.setName(albumDto.getName());
        album.setArtist(artist);
        return albumRepository.save(album);
    }

    public Album updateAlbum(Long id, AlbumDto albumDto) {
        Album album = albumRepository.findById(id).orElseThrow(() -> new AlbumNotFoundException("Album not found"));
        album.setName(albumDto.getName());
        return albumRepository.save(album);
    }

    public void deleteAlbum(Long id) {
        albumRepository.deleteById(id);
    }

    public Album findById(Long id) {
        return albumRepository.findById(id).orElseThrow(() -> new AlbumNotFoundException("Album not found"));
    }
}
