package com.revhub.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    
    private final JavaMailSender mailSender;
    
    public void sendOtpEmail(String to, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("RevHub - Email Verification OTP");
        message.setText("Your OTP for email verification is: " + otp + "\n\nThis OTP will expire in 10 minutes.");
        mailSender.send(message);
    }
    
    public void sendResetPasswordEmail(String to, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("RevHub - Password Reset");
        message.setText("Your password reset token is: " + token + "\n\nThis token will expire in 30 minutes.");
        mailSender.send(message);
    }
}
