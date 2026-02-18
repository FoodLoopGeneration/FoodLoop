package com.generation.foodloop.security;

import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import com.generation.foodloop.entities.Utente;
import com.generation.foodloop.services.UtenteService;

@Component
public class UserPasswordAuthProvider implements AuthenticationProvider{

    @Autowired
    private UtenteService utenteService;

    @Override
    public @Nullable Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String email = authentication.getName();
        String password = authentication.getCredentials().toString();
        Utente utente = utenteService.findUtenteByEmailAndPassword(email, password).orElse(null);
        
        if(utente != null){
            return new UsernamePasswordAuthenticationToken(utente, null, utente.getAuthorities());
        }
        else{
            throw new BadCredentialsException("Credenziali errate");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        
         return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    } 
    
}
