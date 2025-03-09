package com.spotify_clone.spotify_clone.repositories;

import com.spotify_clone.spotify_clone.entities.Role;
import com.spotify_clone.spotify_clone.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(UserRole name);
}