package com.generation.foodloop.entities;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
public class Ricetta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", nullable = false, length = 63, columnDefinition = "VARCHAR(63)", unique = true)
    private String nome;

    // TODO Inserimento immagine
    @Column(name = "immagine")
    private String immagine;

    @Column(name = "difficolta")
    private Integer difficolta;

    @Column(name = "porzioni")
    private Integer porzioni;

    @Column(name = "tempo")
    private Integer tempo;

    @Column(name = "valutazione")
    private Integer valutazione;

    @Column(name = "descrizione")
    private String descrizione;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_utente", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_ricetta_utente"))
    private Utente utente;

    @Column(name = "ingredienti")
    private Set<Ingrediente> ingredienti;

    public void aggiungiIngrediente(Ingrediente ingrediente) {
        if (ingrediente != null) {
            ingredienti.add(ingrediente);
        }
    }

    public void rimuoviIngrediente(Ingrediente ingrediente) {
        if (ingrediente != null) {
            ingredienti.remove(ingrediente);
        }
    }

}
