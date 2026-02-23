package com.generation.foodloop.dto;



import org.springframework.web.multipart.MultipartFile;


import com.generation.foodloop.entities.Utente;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record RicettaDTO(
    Long id,
    
    @NotBlank(message = "Nome obbligatorio")
    String nome,
    
    MultipartFile foto,

    @Min(value = 1, message = "Il valore minimo deve essere 1")
    @Max(value = 5, message = "Il valore massimo deve essere 5")
    Integer difficolta,

    @Min(value = 1, message = "Il valore minimo deve essere 1")
    Integer porzioni,

    @Min(value = 1, message = "Il valore minimo deve essere 1")
    Integer tempo,

    @Min(value = 1, message = "Il valore minimo deve essere 1")
    @Max(value = 5, message = "Il valore massimo deve essere 5")
    Integer valutazione,

    @NotBlank(message = "Descrizione obbligatoria")
    String descrizione,

    Utente utente,

    String ingredienti
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
