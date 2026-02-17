package com.generation.foodloop.utils;

import org.springframework.stereotype.Component;

import com.generation.foodloop.dto.CategoriaDTO;
import com.generation.foodloop.entities.Categoria;

@Component
public class CategoriaMapper {
    public Categoria toEntity(CategoriaDTO dto){
        Categoria c = new Categoria();
        updateEntity(dto,c);
        return c;
    }

    public void updateEntity (CategoriaDTO dto, Categoria c){
        c.setId(dto.id());
        c.setNome(dto.nome());
        c.setUtente(dto.utente());
    }

    public CategoriaDTO toDTO(Categoria c){
        CategoriaDTO dto = new CategoriaDTO (c.getId(),c.getNome(),c.getUtente());
        return dto;
    }
}
