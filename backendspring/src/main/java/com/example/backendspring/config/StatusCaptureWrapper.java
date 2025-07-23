package com.example.backendspring.config;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;

public class StatusCaptureWrapper extends HttpServletResponseWrapper {

    private int httpStatus = 200; // výchozí hodnota

    public StatusCaptureWrapper(HttpServletResponse response) {
        super(response);
    }

    @Override
    public void setStatus(int sc) {
        super.setStatus(sc);
        this.httpStatus = sc;
    }

    @Override
    public void sendError(int sc) {
        super.setStatus(sc);
        this.httpStatus = sc;
    }

    @Override
    public void sendError(int sc, String msg) {
        super.setStatus(sc);
        this.httpStatus = sc;
    }

    public int getStatus() {
        return this.httpStatus;
    }
}
