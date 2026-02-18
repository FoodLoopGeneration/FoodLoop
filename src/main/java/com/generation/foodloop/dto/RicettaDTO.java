package com.generation.foodloop.dto;

import java.util.Set;

import org.springframework.web.multipart.MultipartFile;

import com.generation.foodloop.entities.Ingrediente;
import com.generation.foodloop.entities.Utente;

import jakarta.validation.constraints.NotBlank;

public record RicettaDTO(
    Long id,
    
    @NotBlank(message = "Nome obbligatorio")
    String nome,
    
    // TODO Inserimento immagine
    MultipartFile foto,

    Integer difficolta,

    Integer porzioni,

    Integer tempo,

    Integer valutazione,

    String descrizione,

    Utente utente,

    Set<Ingrediente> ingredienti
) {

    public RicettaDTO{
        nome = (nome == null) ? null : nome.trim();
        descrizione = (descrizione == null) ? null : descrizione.trim();
    }

    public static  RicettaDTO empty(){
        return new RicettaDTO(null, null, null, null, null, null, null, null, null, null);
    }

    public RicettaDTO withId(Long newId){
        return new RicettaDTO(newId, nome, foto, difficolta, porzioni, tempo, valutazione, descrizione, utente, ingredienti);
    }
    
}
