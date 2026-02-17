package com.generation.foodloop.dto;

import java.util.Set;

import org.springframework.web.multipart.MultipartFile;

import com.generation.foodloop.entities.Ingrediente;

import jakarta.validation.constraints.NotBlank;

public record RicettaDTO(
    Long id,
    
    @NotBlank(message = "Nome obbligatorio")
    String nome,
    
    // TODO Inserimento immagine
    MultipartFile immagine,

    String difficolta,

    String porzioni,

    String tempo,

    String valutazione,

    String descrizione,

    Long idUtente,

    Set<Ingrediente> ingredienti
) {

    public RicettaDTO{
        nome = (nome == null) ? null : nome.trim();
        difficolta = (difficolta == null) ? null : difficolta.trim();
        porzioni = (porzioni == null) ? null : porzioni.trim();
        tempo = (tempo == null) ? null : tempo.trim();
        valutazione = (valutazione == null) ? null : valutazione.trim();
        descrizione = (descrizione == null) ? null : descrizione.trim();
    }

    public RicettaDTO empty(){
        return new RicettaDTO(null, null, null, null, null, null, null, null, null, null);
    }

    public RicettaDTO withId(Long newId){
        return new RicettaDTO(newId, nome, immagine, difficolta, porzioni, tempo, valutazione, descrizione, idUtente, ingredienti);
    }
    
}
