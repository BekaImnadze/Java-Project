package com.spotify_clone.spotify_clone.repositories;

import com.spotify_clone.spotify_clone.entities.Role;
import com.spotify_clone.spotify_clone.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(UserRole name);
}
