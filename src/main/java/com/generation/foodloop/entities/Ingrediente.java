package com.generation.foodloop.entities;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Table(name = "ingredienti")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Ingrediente {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    private LocalDate scadenza;
    private Double quantita;

    @Enumerated(EnumType.STRING)
    private UnitaMisura unitaMisura = UnitaMisura.Kg;

    private String posizione;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_utente")
    private Utente utente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_categoria")
    private Categoria categoria;

    @ManyToMany(mappedBy = "ingredienti")
    @ToString.Exclude
    private Set<Ricetta> ricette = new HashSet<>();
}