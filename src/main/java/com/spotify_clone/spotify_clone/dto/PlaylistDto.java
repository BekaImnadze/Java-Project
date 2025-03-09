package com.spotify_clone.spotify_clone.dto;

import lombok.Data;

import java.util.List;

@Data
public class PlaylistDto {
    private String name;
    private List<Long> musicIds;
}
