package com.generation.foodloop.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.generation.foodloop.dto.CategoriaDTO;
import com.generation.foodloop.entities.Categoria;
import com.generation.foodloop.entities.Utente;
import com.generation.foodloop.repositories.CategoriaRepository;
import com.generation.foodloop.utils.CategoriaMapper;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CategoriaService extends GenericService<Long, Categoria, CategoriaRepository>{
    
    private final CategoriaMapper mapper;

    private String normNome(String nome){
        return nome == null ? null : nome.trim().toUpperCase();
    }

    public Map<String, String> uniqueErrorsForCreate(CategoriaDTO dto){
        Map<String, String> errors = new HashMap<>();
        String nome = normNome(dto.nome());
        if(nome != null && getRepository().existsByNome(nome)){
            errors.put("nome", "Nome già presente");
        }
        return errors;
    }

    public Map<String, String> uniqueErrorsForUpdate(Long id, CategoriaDTO dto){
        Map<String, String> errors = new HashMap<>();
        String nome = normNome(dto.nome());
        if(nome != null && getRepository().existsByNomeAndId(nome, id)){
            errors.put("nome", "Nome già presente");
        }
        return errors;
    }

    @Transactional
    public boolean createFromDto(CategoriaDTO dto, Utente autore){
        Categoria c = mapper.toEntity(dto);
        c.setUtente(autore);
        getRepository().save(c);
        return true;
    }

    @Transactional
    public boolean updateFromDto(Long id, CategoriaDTO dto){
        Categoria c = getByIdOrNull(id);
        if(c == null){
            return false;
        }
        mapper.updateEntity(dto, c);
        getRepository().save(c);
        return true;
    }

    public boolean delete(Long id){
        Categoria c = getByIdOrNull(id);
        if(c == null){
            return false;
        }
        deleteById(id);
        return true;
    }

    public CategoriaDTO getDTOById(Long id){
        Categoria c = getByIdOrNull(id);
        return c == null ? null : mapper.toDTO(c);
    }

    public List<Categoria> getByUtente(Long utenteId) {
        return getRepository().findByUtenteId(utenteId);
    }

}
