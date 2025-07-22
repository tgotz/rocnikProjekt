package com.example.backendspring.service;

import com.example.backendspring.model.OtpToken;
import com.example.backendspring.repository.OtpTokenRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class OtpService {

    private final OtpTokenRepository otpTokenRepository;
    private final EmailService emailService;


    @Transactional
    public void generateAndSendOtp(String email) {
        String otp = String.format("%06d", new Random().nextInt(999999));
        LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(10);

        otpTokenRepository.deleteByEmail(email); // čistka

        OtpToken otpToken = new OtpToken(null, email, otp, expirationTime);
        otpTokenRepository.save(otpToken);

        emailService.sendOtpEmail(email, otp);
    }

    public boolean verifyOtp(String email, String otp) {
        return otpTokenRepository.findByEmailAndOtpCode(email, otp)
                .filter(token -> token.getExpirationTime().isAfter(LocalDateTime.now()))
                .isPresent();
    }
}
