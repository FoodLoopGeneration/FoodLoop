package com.generation.foodloop.dto;

import java.util.Set;

import com.generation.foodloop.entities.Ingrediente;
import com.generation.foodloop.entities.Ricetta;

public record UtenteDTO(Long id, Set<Ingrediente> ingredienti, Set<Ricetta> ricette, String nome, String cognome, String email, String password) {
    
}
