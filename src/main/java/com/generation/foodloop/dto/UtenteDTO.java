package com.generation.foodloop.dto;

import java.util.Set;

import com.generation.foodloop.entities.Ingrediente;
import com.generation.foodloop.entities.Ricetta;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UtenteDTO(
    Long id, 

    Set<Ingrediente> ingredienti, 

    Set<Ricetta> ricette, 

    @NotBlank(message = "Nome obbligatorio")
    String nome, 

    @NotBlank(message = "Cognome  obbligatorio")
    String cognome, 

    @Email(message = "Email non valida")
    @NotBlank(message = "Email obbligatoria")
    String email
) {
    
    public UtenteDTO{
        nome = (nome == null) ? null : nome.trim();
        cognome = (cognome == null) ? null : cognome.trim();
        email = (email == null) ? null : email.trim();
    }

    public static UtenteDTO empty(){
        return new UtenteDTO(null, null, null, null, null, null);
    }

    public UtenteDTO withId(Long newId){
        return new UtenteDTO(newId, null, null, null, null, null);
    }
}
