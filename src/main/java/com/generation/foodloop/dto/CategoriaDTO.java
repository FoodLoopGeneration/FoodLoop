package com.generation.foodloop.dto;

import jakarta.validation.constraints.NotBlank;

public record CategoriaDTO(
    Long id,
    
    @NotBlank(message = "Nome obbligatorio")
    String nome,

    Long idUtente
) {

    public CategoriaDTO{
        nome = (nome == null) ? null : nome.trim();
    }

    public CategoriaDTO empty(){
        return new CategoriaDTO(null, null, null);
    }

    public CategoriaDTO withId(Long newId){
        return new CategoriaDTO(newId, nome, idUtente);
    }
    
}
