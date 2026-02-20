package com.generation.foodloop.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;

import com.generation.foodloop.entities.TipoRuolo;
import com.generation.foodloop.security.filters.CookieFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Autowired
    private UserPasswordAuthProvider userPasswordAuthProvider;

    @Autowired
    private CookieFilter cookieFilter;

    @Autowired
    private LoginSuccessHandler loginSuccessHandler;

    @Autowired
    private LogoutHandler logoutHandler;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http

            .csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**"))
            
            .authenticationProvider(userPasswordAuthProvider)
            
            .addFilterBefore(cookieFilter, UsernamePasswordAuthenticationFilter.class)
            
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(ApiRoutes.PUBLIC_ENDPOINTS).permitAll()
                .requestMatchers(ApiRoutes.USER_ENDPOINTS).hasAnyRole(TipoRuolo.USR.name(), TipoRuolo.ADM.name())
                .requestMatchers(ApiRoutes.ADMIN_ENDPOINTS).hasRole(TipoRuolo.ADM.name())
                .anyRequest().authenticated()
            )
            
            .formLogin(login -> login
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .successHandler(loginSuccessHandler)
                .failureUrl("/login?error=true")
                .permitAll()
            )
            
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessHandler(logoutHandler)
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .deleteCookies("JSESSIONID", "FOODLOOP_AUTH")
                .logoutRequestMatcher(PathPatternRequestMatcher.pathPattern(HttpMethod.GET,"/logout"))
                .permitAll()
            );

        return http.build();
    }
}