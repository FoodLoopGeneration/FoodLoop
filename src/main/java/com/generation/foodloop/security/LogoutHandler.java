package com.generation.foodloop.security;

import java.io.IOException;

import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class LogoutHandler implements LogoutSuccessHandler {
    @Value("${app.cookie.nome}")
    public String cookieName;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
            @Nullable Authentication authentication) throws IOException, ServletException {
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        response.sendRedirect("/");

    }
}