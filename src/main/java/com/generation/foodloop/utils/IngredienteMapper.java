package com.generation.foodloop.utils;

import org.springframework.stereotype.Component;

import com.generation.foodloop.dto.IngredienteDTO;
import com.generation.foodloop.entities.Ingrediente;

@Component
public class IngredienteMapper {
    public Ingrediente toEntity(IngredienteDTO dto) {
        Ingrediente i = new Ingrediente();
        updateEntity(dto, i);
        return i;
    }

    public void updateEntity(IngredienteDTO dto, Ingrediente i) {
        i.setId(dto.id());
        i.setCategoria(dto.categoria());
        i.setUtente(dto.utente());
        i.setNome(dto.nome());
        i.setScadenza(dto.scadenza());
        i.setQuantita(dto.quantita());
        i.setUnitaMisura(dto.unitaMisura());
        i.setPosizione(dto.posizione());
    }

    public IngredienteDTO toDTO(Ingrediente i) {
        IngredienteDTO dto = new IngredienteDTO(i.getId(), i.getCategoria(), i.getUtente(), i.getNome(), i.getScadenza(), i.getQuantita(), i.getUnitaMisura(), i.getPosizione());
        return dto;
    }
}
