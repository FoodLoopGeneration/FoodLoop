package com.generation.foodloop.dto;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import com.generation.foodloop.entities.UnitaMisura;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record IngredienteDTO(
    Long id,

    @NotBlank(message = "Nome obbligatorio")
    String nome,

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "Scadenza obbligatoria")
    LocalDate scadenza,

    @NotNull(message = "Quantità obbligatoria")
    Double quantita,

    @NotBlank(message = "Unità di misura obbligatoria")
    UnitaMisura unitaMisura,

    String posizione,

    Long idUtente,

    Long idCategoria
) {
    
    public IngredienteDTO{
        nome = (nome == null) ? null : nome.trim();
        posizione = (posizione == null) ? null : posizione.trim();
    }

    public IngredienteDTO empty(){
        return new IngredienteDTO(null, null, null, null, null, null, null, null);
    }

    public IngredienteDTO withId(Long newId){
        return new IngredienteDTO(newId, nome, scadenza, quantita, unitaMisura, posizione, idUtente, idCategoria);
    }

}
