package com.revhub.user.repository;

import com.revhub.user.entity.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    Optional<VerificationToken> findByEmailAndOtpAndUsedFalse(String email, String otp);
    void deleteByEmail(String email);
}
