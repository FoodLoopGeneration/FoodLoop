package com.generation.foodloop.entities;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Table(name = "utenti")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Utente implements UserDetails {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "nome")
    private String nome;

    @Column(nullable = false)
    private String cognome;

    @EqualsAndHashCode.Include
    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "utente", cascade = CascadeType.ALL)
    @ToString.Exclude
    private Set<Ingrediente> ingredienti = new HashSet<>();

    @OneToMany(mappedBy = "utente", cascade = CascadeType.ALL)
    @ToString.Exclude
    private Set<Ricetta> ricette = new HashSet<>();

    @OneToMany(mappedBy = "utente", cascade = CascadeType.ALL)
    @ToString.Exclude
    private Set<Categoria> categorie = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "utenti_ruoli",
        joinColumns = @JoinColumn(name = "id_utente"),
        inverseJoinColumns = @JoinColumn(name = "id_ruolo")
    )
    private Set<Ruolo> ruoli = new HashSet<>();

    // Metodi Helper
    public void aggiungiRuolo(Ruolo ruolo) { if (ruolo != null) ruoli.add(ruolo); }
    public void aggiungiIngrediente(Ingrediente i) { i.setUtente(this); ingredienti.add(i); }
    public void aggiungiRicetta(Ricetta r) { r.setUtente(this); ricette.add(r); }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return ruoli.stream()
                .map(r -> new SimpleGrantedAuthority("ROLE_" + r.getNome()))
                .toList();
    }

    @Override public String getUsername() { return email; }
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}