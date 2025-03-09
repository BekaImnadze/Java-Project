package com.spotify_clone.spotify_clone.controller;

import com.spotify_clone.spotify_clone.config.JwtUtil;
import com.spotify_clone.spotify_clone.dto.UserDto;
import com.spotify_clone.spotify_clone.entities.User;
import com.spotify_clone.spotify_clone.enums.UserRole;
import com.spotify_clone.spotify_clone.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserDto userDto, @RequestParam UserRole role) {
        User user = userService.register(userDto, role);
        return ResponseEntity.ok(Map.of("message", "User registered successfully, please verify your email."));
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verify(@RequestParam String email, @RequestParam String code) {
        userService.verifyEmail(email, code);
        return ResponseEntity.ok(Map.of("message", "Email verified successfully."));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDto userDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userDto.getUsername(), userDto.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtUtil.generateToken(authentication);
        return ResponseEntity.ok(Map.of("token", token));
    }

    @GetMapping("/profile")
    public ResponseEntity<?> profile(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        String username = jwtUtil.getUsernameFromToken(token);
        User user = userService.findByUsername(username);
        return ResponseEntity.ok(user);
    }
}
