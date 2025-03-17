package com.example.backendspring.repository;

import com.example.backendspring.model.OtpToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OtpTokenRepository extends JpaRepository<OtpToken, Long> {
    Optional<OtpToken> findByEmail(String email);
    Optional<OtpToken> findByEmailAndOtpCode(String email, String otpCode);
    void deleteByEmail(String email); // cleanup před novým generováním
}
