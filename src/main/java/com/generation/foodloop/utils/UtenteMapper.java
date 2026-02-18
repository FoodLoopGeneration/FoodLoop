package com.generation.foodloop.utils;

import org.springframework.stereotype.Component;

import com.generation.foodloop.dto.UtenteDTO;
import com.generation.foodloop.entities.Utente;

@Component
public class UtenteMapper {
    public Utente toEntity(UtenteDTO dto){
        Utente u = new Utente();

        return u;
    }

    public void updateEntity(UtenteDTO dto, Utente u){
        u.setId(dto.id());
        u.setIngredienti(dto.ingredienti());
        u.setRicette(dto.ricette());
        u.setNome(dto.nome());
        u.setCognome(dto.cognome());
        u.setEmail(dto.email());
    }

    public UtenteDTO toDTO(Utente u){
        UtenteDTO dto = new UtenteDTO(u.getId(),u.getIngredienti(),u.getRicette(),u.getNome(),u.getCognome(),u.getEmail());
        return dto;
    }
}
