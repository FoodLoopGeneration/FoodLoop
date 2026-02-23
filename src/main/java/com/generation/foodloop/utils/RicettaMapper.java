package com.generation.foodloop.utils;

import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import com.generation.foodloop.dto.RicettaDTO;
import com.generation.foodloop.entities.Ricetta;

@Component
public class RicettaMapper {


    public Ricetta toEntity(RicettaDTO dto) {
        Ricetta r = new Ricetta();
        updateEntity(dto, r);
        return r;
    }


    public void updateEntity(RicettaDTO dto, Ricetta r) {
        r.setId(dto.id());
        r.setNome(dto.nome());
        r.setDifficolta(dto.difficolta());
        r.setPorzioni(dto.porzioni());
        r.setTempo(dto.tempo());
        r.setValutazione(dto.valutazione());
        r.setDescrizione(dto.descrizione());

    }


    public RicettaDTO toDTO(Ricetta r) {
        String ingredientiIds = "";
        if (r.getIngredienti() != null && !r.getIngredienti().isEmpty()) {
            ingredientiIds = r.getIngredienti().stream()
                    .map(ing -> ing.getId().toString())
                    .collect(Collectors.joining(","));
        }

        return new RicettaDTO(
                r.getId(),
                r.getNome(),
                null, 
                r.getDifficolta(),
                r.getPorzioni(),
                r.getTempo(),
                r.getValutazione(),
                r.getDescrizione(),
                r.getUtente(),
                ingredientiIds
        );
    }
}