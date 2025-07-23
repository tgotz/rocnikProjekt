package com.example.backendspring.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class RequestLoggingFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger("HttpRequestLogger");

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;

        // ⏱️ Začátek měření času
        long start = System.currentTimeMillis();

        // 🧪 Obalíme response, abychom zachytili status
        StatusCaptureWrapper wrappedResponse = new StatusCaptureWrapper((jakarta.servlet.http.HttpServletResponse) response);

        chain.doFilter(request, wrappedResponse);  // Pokračuj filtrovacím řetězcem

        long duration = System.currentTimeMillis() - start;

        String logEntry = String.format(
                "[%s] %s %s | Status: %d | Origin: %s | Referer: %s | UA: %s | Duration: %d ms",
                ZonedDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME),
                req.getMethod(),
                req.getRequestURI(),
                wrappedResponse.getStatus(),
                req.getHeader("Origin"),
                req.getHeader("Referer"),
                req.getHeader("User-Agent"),
                duration
        );

        logger.info(logEntry);
    }
}