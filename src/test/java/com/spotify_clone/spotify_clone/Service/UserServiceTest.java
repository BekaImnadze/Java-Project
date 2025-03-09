package com.spotify_clone.spotify_clone.Service;

import com.spotify_clone.spotify_clone.config.JwtUtil;
import com.spotify_clone.spotify_clone.dto.UserDto;
import com.spotify_clone.spotify_clone.entities.Role;
import com.spotify_clone.spotify_clone.entities.User;
import com.spotify_clone.spotify_clone.enums.UserRole;
import com.spotify_clone.spotify_clone.enums.UserStatus;
import com.spotify_clone.spotify_clone.repositories.RoleRepository;
import com.spotify_clone.spotify_clone.repositories.UserRepository;
import com.spotify_clone.spotify_clone.service.EmailService;
import com.spotify_clone.spotify_clone.service.UserService;
import com.spotify_clone.spotify_clone.util.RandomCodeGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private EmailService emailService;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private RandomCodeGenerator randomCodeGenerator;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegister() {
        UserDto userDto = new UserDto();
        userDto.setUsername("testuser");
        userDto.setPassword("password");
        userDto.setEmail("test@example.com");

        Role role = new Role();
        role.setName(UserRole.LISTENER);
        when(roleRepository.findByName(UserRole.LISTENER)).thenReturn(Optional.of(role));
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(randomCodeGenerator.generateCode()).thenReturn("123456");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User user = userService.register(userDto, UserRole.LISTENER);

        assertEquals("testuser", user.getUsername());
        assertEquals("encodedPassword", user.getPassword());
        assertEquals("test@example.com", user.getEmail());
        assertEquals(UserStatus.PENDING, user.getStatus());
        assertEquals("123456", user.getVerificationCode());
        verify(emailService).sendVerificationEmail("test@example.com", "123456");
    }

    @Test
    public void testVerifyEmail() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setVerificationCode("123456");
        user.setStatus(UserStatus.PENDING);
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        userService.verifyEmail("test@example.com", "123456");

        assertEquals(UserStatus.ACTIVE, user.getStatus());
    }

    @Test
    public void testLogin() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("encodedPassword");
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password", "encodedPassword")).thenReturn(true);

        User result = userService.login("testuser", "password");

        assertEquals("testuser", result.getUsername());
    }
}