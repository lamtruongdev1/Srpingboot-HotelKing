package com.hotelking.service;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

@Service
public  class  SessionService {

    private  final HttpSession httpSession;

    public  SessionService(HttpSession httpSession) {
        this.httpSession = httpSession;
    }

    public  void setAttribute(String name, Object value) {
        httpSession.setAttribute(name, value);
    }

    public Object getAttribute(String name) {
        return httpSession.getAttribute(name);
    }

    public void removeAttribute(String name) {
        httpSession.removeAttribute(name);
    }

    public void invalidate() {
        httpSession.invalidate();
    }
}