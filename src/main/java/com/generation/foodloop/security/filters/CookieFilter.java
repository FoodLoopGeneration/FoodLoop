package com.generation.foodloop.security.filters;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.generation.foodloop.entities.Utente;
import com.generation.foodloop.services.UtenteService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CookieFilter extends OncePerRequestFilter {

    @Value("${app.cookie.nome}")
    private String cookieName;

    private final UtenteService utenteService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        Cookie[] cookies = request.getCookies();
        Cookie myCookie = getCookie(cookies, cookieName);

        if (myCookie == null || myCookie.getValue().isEmpty()) {
            if (SecurityContextHolder.getContext().getAuthentication() != null) {
                SecurityContextHolder.clearContext();
            }
        } else if (SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                String[] parts = myCookie.getValue().split(":");
                if (parts.length >= 2) {
                    Long id = Long.parseLong(parts[0]);
                    Utente user = utenteService.findById(id).orElse(null);
                    if (user != null) {
                        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user, null,
                                user.getAuthorities());
                        SecurityContextHolder.getContext().setAuthentication(auth);
                    }
                }
            } catch (Exception e) {
                SecurityContextHolder.clearContext();
            }
        }
        filterChain.doFilter(request, response);
    }

    private Cookie getCookie(Cookie[] cookies, String name) {
        if (cookies == null)
            return null;
        for (Cookie c : cookies) {
            if (c.getName().equals(name))
                return c;
        }
        return null;
    }
}