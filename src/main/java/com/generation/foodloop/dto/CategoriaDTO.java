package com.generation.foodloop.dto;

import jakarta.validation.constraints.NotBlank;
import com.generation.foodloop.entities.Utente;

public record CategoriaDTO(
    Long id,
    
    @NotBlank(message = "Nome obbligatorio")
    String nome,

    Utente utente
) {

    public CategoriaDTO{
        nome = (nome == null) ? null : nome.trim();
    }

    public static CategoriaDTO empty(){
        return new CategoriaDTO(null, null, null);
    }

    public CategoriaDTO withId(Long newId){
        return new CategoriaDTO(newId, nome, utente);
    }
    
}
