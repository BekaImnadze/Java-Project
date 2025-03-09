package com.spotify_clone.spotify_clone.service;

import com.spotify_clone.spotify_clone.config.JwtUtil;
import com.spotify_clone.spotify_clone.dto.UserDto;
import com.spotify_clone.spotify_clone.entities.Role;
import com.spotify_clone.spotify_clone.entities.User;
import com.spotify_clone.spotify_clone.enums.UserRole;
import com.spotify_clone.spotify_clone.enums.UserStatus;
import com.spotify_clone.spotify_clone.exception.UserNotFoundException;
import com.spotify_clone.spotify_clone.repositories.RoleRepository;
import com.spotify_clone.spotify_clone.repositories.UserRepository;
import com.spotify_clone.spotify_clone.util.RandomCodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RandomCodeGenerator randomCodeGenerator;


    public User register(UserDto userDto, UserRole role) {
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setEmail(userDto.getEmail());
        user.setStatus(UserStatus.PENDING);
        user.setVerificationCode(randomCodeGenerator.generateCode());

        Optional<Role> userRole = roleRepository.findByName(role);
        Set<Role> roles = new HashSet<>();
        if (userRole.isPresent()) {
            roles.add(userRole.get());
        }
        user.setRoles(roles);

        userRepository.save(user);
        emailService.sendVerificationEmail(user.getEmail(), user.getVerificationCode());
        return user;
    }

    public void verifyEmail(String email, String code) {
        User user = (User) userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found"));
        if (user.getVerificationCode().equals(code)) {
            user.setStatus(UserStatus.ACTIVE);
            userRepository.save(user);
        } else {
            throw new RuntimeException("Invalid verification code");
        }
    }

    public User login(String username, String password) {
        User user = (User) userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User not found"));
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }
        return user;
    }

    public User updateUser(Long id, UserDto userDto) {
        User user = (User) userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        userRepository.save(user);
        return user;
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public void blockUser(Long id) {
        User user = (User) userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));
        user.setStatus(UserStatus.BLOCKED);
        userRepository.save(user);
    }

    public List<User> searchUsers(String query) {
        return userRepository.findByUsernameContainingIgnoreCase(query);
    }

    public List<User> findAll() {
        return userRepository.findAll(); // Added findAll() method

    }
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    public Authentication loadUserByUsername(String username) {
        return userRepository.loadByUsername(username);
    }
}
