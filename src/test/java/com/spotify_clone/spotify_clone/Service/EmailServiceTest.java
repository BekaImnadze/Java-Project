package com.spotify_clone.spotify_clone.Service;

import com.spotify_clone.spotify_clone.service.EmailService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailService emailService;

    @Test
    void sendVerificationEmail_ShouldSendEmail() {
        ReflectionTestUtils.setField(emailService, "fromEmail", "test@example.com");

        emailService.sendVerificationEmail("to@example.com", "123456");

        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }
}
