package com.revhub.user.service;

import com.revhub.user.dto.*;
import com.revhub.user.entity.User;
import com.revhub.user.repository.UserRepository;
import com.revhub.user.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final EmailService emailService;
    
    public String register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already taken. Please choose a different username.");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered. Please use a different email or login.");
        }
        
        String otp = generateOtp();
        
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setIsVerified(true);
        user.setVerificationOtp(otp);
        user.setOtpExpiry(LocalDateTime.now().plusMinutes(10));
        
        userRepository.save(user);
        System.out.println("OTP for " + user.getEmail() + ": " + otp);
        
        try {
            emailService.sendOtpEmail(user.getEmail(), otp);
            return "Registration successful. Please check your email for OTP.";
        } catch (Exception e) {
            System.err.println("Email sending failed: " + e.getMessage());
            return "Registration successful. You can now login.";
        }
    }
    
    public JwtResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));
        
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }
        
        if (!user.getIsVerified()) {
            throw new RuntimeException("Email not verified. Please verify your email first.");
        }
        
        String token = jwtUtil.generateToken(user.getUsername(), user.getId());
        return new JwtResponse(token, user.getId(), user.getUsername());
    }
    
    public JwtResponse verifyOtp(VerifyOtpRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        if (user.getVerificationOtp() == null || !user.getVerificationOtp().equals(request.getOtp())) {
            throw new RuntimeException("Invalid OTP");
        }
        
        if (user.getOtpExpiry().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("OTP expired");
        }
        
        user.setIsVerified(true);
        user.setVerificationOtp(null);
        user.setOtpExpiry(null);
        userRepository.save(user);
        
        String token = jwtUtil.generateToken(user.getUsername(), user.getId());
        return new JwtResponse(token, user.getId(), user.getUsername());
    }
    
    public String resendOtp(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        if (user.getIsVerified()) {
            throw new RuntimeException("Email already verified");
        }
        
        String otp = generateOtp();
        user.setVerificationOtp(otp);
        user.setOtpExpiry(LocalDateTime.now().plusMinutes(10));
        userRepository.save(user);
        
        emailService.sendOtpEmail(user.getEmail(), otp);
        return "OTP sent successfully";
    }
    
    public String forgotPassword(ForgotPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        String token = UUID.randomUUID().toString();
        user.setResetToken(token);
        user.setResetTokenExpiry(LocalDateTime.now().plusMinutes(30));
        userRepository.save(user);
        
        emailService.sendResetPasswordEmail(user.getEmail(), token);
        return "Password reset token sent to your email";
    }
    
    public String resetPassword(ResetPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        if (user.getResetToken() == null || !user.getResetToken().equals(request.getToken())) {
            throw new RuntimeException("Invalid reset token");
        }
        
        if (user.getResetTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Reset token expired");
        }
        
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setResetToken(null);
        user.setResetTokenExpiry(null);
        userRepository.save(user);
        
        return "Password reset successfully";
    }
    
    private String generateOtp() {
        return String.format("%06d", new Random().nextInt(999999));
    }
}
