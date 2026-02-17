package com.generation.foodloop.services;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.generation.foodloop.dto.RicettaDTO;
import com.generation.foodloop.entities.Ricetta;
import com.generation.foodloop.repositories.RicettaRepository;
import com.generation.foodloop.utils.RicettaMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class RicettaService extends GenericService<Long, Ricetta, RicettaRepository>{
    
    private final RicettaMapper mapper;

    private String normNome(String nome){
        return nome == null ? null : nome.trim().toUpperCase();
    }

    public Map<String, String> uniqueErrorsForCreate(RicettaDTO dto){
        Map<String, String> errors = new HashMap<>();
        String nome = normNome(dto.nome());
        if(nome != null && getRepository().existsByNome(nome)){
            errors.put("nome", "Nome già presente");
        }
        return errors;
    }

    public Map<String, String> uniqueErrorsForUpdate(Long id, RicettaDTO dto){
        Map<String, String> errors = new HashMap<>();
        String nome = normNome(dto.nome());
        if(nome != null && getRepository().existsByNomeAndId(nome, id)){
            errors.put("nome", "Nome già presente");
        }
        return errors;
    }

    public boolean createFromDto(RicettaDTO dto){
        Ricetta r = mapper.toEntity(dto);
        getRepository().save(r);
        return true;
    }

    public boolean updateFromDto(Long id, RicettaDTO dto){
        Ricetta r = getByIdOrNull(id);
        if(r == null){
            return false;
        }
        mapper.updateEntity(dto, r);
        getRepository().save(r);
        return true;
    }

    public boolean delete(Long id){
        Ricetta r = getByIdOrNull(id);
        if(r == null){
            return false;
        }
        deleteById(id);
        return true;
    }

    public RicettaDTO getDTOById(Long id){
        Ricetta r = getByIdOrNull(id);
        return r == null ? null : mapper.toDTO(r);
    }

}
