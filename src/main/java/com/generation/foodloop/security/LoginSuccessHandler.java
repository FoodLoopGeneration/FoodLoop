package com.generation.foodloop.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.generation.foodloop.entities.Utente;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Value("${app.cookie.nome}")
    private String cookieName;
    @Value("${app.cookie.age}")
    private int cookieMaxAge;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        Utente user = (Utente) authentication.getPrincipal();
        Cookie cookie = new Cookie(cookieName, user.getId() + ":" + user.getUsername() + ":SIGNITURE");
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(cookieMaxAge);
        response.addCookie(cookie);
        response.sendRedirect("/ingredienti");

    }

}
