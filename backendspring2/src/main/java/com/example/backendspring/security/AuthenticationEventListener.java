package com.example.backendspring.security;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Component
public class AuthenticationEventListener {

    private static final Logger logger = LoggerFactory.getLogger("login-audit");

    @Autowired
    private HttpServletRequest request;

    @EventListener
    public void onSuccess(AuthenticationSuccessEvent event) {
        String username = event.getAuthentication().getName();
        String ip = getClientIP();
        logger.info("LOGIN SUCCESS: user={}, ip={}", username, ip);
    }

    @EventListener
    public void onFailure(AbstractAuthenticationFailureEvent event) {
        String username = Optional.ofNullable(event.getAuthentication().getPrincipal())
                .map(Object::toString)
                .orElse("UNKNOWN");
        String ip = getClientIP();
        logger.warn("LOGIN FAIL: user={}, ip={}, reason={}", username, ip, event.getException().getMessage());
    }

    private String getClientIP() {
        String xfHeader = request.getHeader("X-Forwarded-For");
        return xfHeader != null ? xfHeader.split(",")[0] : request.getRemoteAddr();
    }
}
