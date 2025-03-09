package com.spotify_clone.spotify_clone.entities;

import com.spotify_clone.spotify_clone.enums.UserStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String email;
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles;
    @Enumerated(EnumType.STRING)
    private UserStatus status;
    private String verificationCode;
}