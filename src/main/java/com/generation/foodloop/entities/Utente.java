package com.generation.foodloop.entities;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;
import lombok.EqualsAndHashCode;

 @Entity
    @Table(name = "utenti", uniqueConstraints = {
        @UniqueConstraint(columnNames = "id"),
        @UniqueConstraint(columnNames = "email")
    })
    @Data
    @EqualsAndHashCode(onlyExplicitlyIncluded = true)

public class Utente implements UserDetails{

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable=false)
    private String nome;

    @Column(nullable=false)
    private String cognome;

    @EqualsAndHashCode.Include
    @Column(nullable=false, unique = true)
    private String email;

    @Column(nullable=false)
    private String password;

    @Column(nullable=false)
    private Set<Ingrediente> ingredienti;

    @Column(nullable=false)
    private Set<Ricetta> ricette;

    private Set<Ruolo> ruoli = new HashSet<>(); 

    public void aggiungiRuolo(Ruolo ruolo){
        if(ruolo != null){
            ruoli.add(ruolo);
        }
    }

    public void rimuoviRuolo(Ruolo ruolo){
        if(ruolo != null){
            ruoli.remove(ruolo);
        }
    }

    public void aggiungiIngrediente(Ingrediente ingrediente){
        if(ingrediente != null){
            ingredienti.add(ingrediente);
        }
    }

    public void rimuoviIngrediente(Ingrediente ingrediente){
        if(ingrediente != null){
            ingredienti.remove(ingrediente);
        }
    }

    public void aggiungiRicetta(Ricetta ricetta){
        if(ricetta != null){
            ricette.add(ricetta);
        }
    }

    public void rimuoviRicetta(Ricetta ricetta){
        if(ricetta != null){
            ricette.remove(ricetta);
        }
    }
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = this.ruoli.stream()
            .map(
                ruolo -> new SimpleGrantedAuthority("ROLE_" + ruolo.getNome())
            )
            .toList();

        return authorities;
    }

    @Override
    public String getUsername() {
        return this.email;
    }
    
}
