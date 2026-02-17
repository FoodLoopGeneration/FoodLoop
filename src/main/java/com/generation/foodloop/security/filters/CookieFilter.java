package com.generation.foodloop.security.filters;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class CookieFilter extends OncePerRequestFilter {
    @Value("${app.cookie.nome}")
    private String cookieName;

    private final UtenteService userAccountService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            filterChain.doFilter(request, response);
            return;
        }
        Cookie cookie = getCookie(request.getCookies(), cookieName);
        if (cookie != null) {
            Long id = Long.parseLong(cookie.getValue().split(":")[0]);
            Utente user = userAccountService.findById(id).orElse(null);
            if (user != null) {
                List<SimpleGrantedAuthority> authorities = user.getRuoli().stream()
                        .map(ruolo -> new SimpleGrantedAuthority("ROLE_" + ruolo.getNome())).toList();
                auth = new UsernamePasswordAuthenticationToken(user, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(auth);
                log.info("utente {} autenticato tramite cookie", user.getUsername());
            }
            filterChain.doFilter(request, response);
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        return path.equals("/login") || path.startsWith("/css") || path.startsWith("/js");
    }

    private Cookie getCookie(Cookie[] cookies, String cookieName) {
        for (Cookie c : cookies) {
            if (cookieName.equals(c.getName())) {
                return c;
            }
        }
        return null;
    }
}
