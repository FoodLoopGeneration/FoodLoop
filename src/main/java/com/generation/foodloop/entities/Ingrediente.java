package com.generation.foodloop.entities;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "ingredienti")
@Data
@EqualsAndHashCode
public class Ingrediente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", nullable = false, length = 63, columnDefinition = "VARCHAR(63)")
    private String nome;

    @Column(name = "scadenza", nullable = false)
    private LocalDate scadenza;

    @Column(name = "quantita", nullable = false)
    private Double quantita;

    @Enumerated(EnumType.STRING)
    @Column(name = "unità_misura", nullable = false)
    private UnitaMisura unitaMisura = UnitaMisura.Kg;

    @Column(name = "posizione", length = 255, columnDefinition = "VARCHAR(255)")
    private String posizione;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_utente", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_ingrediente_utente"))
    private Utente utente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_categoria", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_ingrediente_categoria"))
    private Categoria categoria;
    
}
