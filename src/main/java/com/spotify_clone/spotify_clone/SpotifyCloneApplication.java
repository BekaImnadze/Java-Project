package com.spotify_clone.spotify_clone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SpotifyCloneApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpotifyCloneApplication.class, args);
	}

}
