package com.generation.foodloop.entities;

import java.util.HashSet;
import java.util.Set;
import org.springframework.web.multipart.MultipartFile;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Table(name = "ricette")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Ricetta {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nome;

    private String foto; // Salva il path/nome del file nel DB

    @Transient
    private MultipartFile fileFoto;

    private Integer difficolta;
    private Integer porzioni;
    private Integer tempo;
    private Integer valutazione;

    @Column(columnDefinition = "TEXT")
    private String descrizione;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_utente", foreignKey = @ForeignKey(name = "fk_ricetta_utente"))
    private Utente utente;

    @ManyToMany
    @JoinTable(
        name = "ricette_ingredienti",
        joinColumns = @JoinColumn(name = "id_ricetta"),
        inverseJoinColumns = @JoinColumn(name = "id_ingrediente")
    )
    @ToString.Exclude
    private Set<Ingrediente> ingredienti = new HashSet<>();

    public void aggiungiIngrediente(Ingrediente i) { if (i != null) ingredienti.add(i); }
}