package com.generation.foodloop.services;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.generation.foodloop.dto.IngredienteDTO;
import com.generation.foodloop.entities.Ingrediente;
import com.generation.foodloop.repositories.IngredienteRepository;
import com.generation.foodloop.utils.IngredienteMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class IngredienteService extends GenericService<Long, Ingrediente, IngredienteRepository>{
    
    private final IngredienteMapper mapper;

    public boolean createFromDto(IngredienteDTO dto){
        Ingrediente i = mapper.toEntity(dto);
        getRepository().save(i);
        return true;
    }

    public boolean updateFromDto(Long id, IngredienteDTO dto){
        Ingrediente i = getByIdOrNull(id);
        if(i == null){
            return false;
        }
        mapper.updateEntity(dto, i);
        getRepository().save(i);
        return true;
    }

    public boolean delete(Long id){
        Ingrediente i = getByIdOrNull(id);
        if(i == null){
            return false;
        }
        deleteById(id);
        return true;
    }

    public IngredienteDTO getDTOById(Long id){
        Ingrediente i = getByIdOrNull(id);
        return i == null ? null : mapper.toDTO(i);
    }

}
