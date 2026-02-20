package com.generation.foodloop.services;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

import org.springframework.stereotype.Service;

import com.generation.foodloop.dto.IngredienteDTO;
import com.generation.foodloop.entities.Ingrediente;
import com.generation.foodloop.entities.Utente;
import com.generation.foodloop.entities.Categoria;
import com.generation.foodloop.repositories.IngredienteRepository;
import com.generation.foodloop.utils.IngredienteMapper;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Slf4j
public class IngredienteService extends GenericService<Long, Ingrediente, IngredienteRepository> {

    private final IngredienteMapper mapper;
    private final CategoriaService categoriaService;

    private String normNome(String nome) {
        return nome == null ? null : nome.trim().toUpperCase();
    }

    public Map<String, String> uniqueErrorsForCreate(IngredienteDTO dto) {
        Map<String, String> errors = new HashMap<>();
        String nome = normNome(dto.nome());
        if (nome != null && getRepository().existsByNome(nome)) {
            errors.put("nome", "Ingrediente già presente in archivio");
        }
        return errors;
    }

    public Map<String, String> uniqueErrorsForUpdate(Long id, IngredienteDTO dto) {
        Map<String, String> errors = new HashMap<>();
        String nome = normNome(dto.nome());
        if (nome != null && getRepository().existsByNomeAndIdNot(nome, id)) {
            errors.put("nome", "Il nome scelto è già utilizzato da un altro ingrediente");
        }
        return errors;
    }

    public List<Ingrediente> getAll() {
        return getRepository().findAll();
    }

    @Transactional
    public boolean createFromDto(IngredienteDTO dto, Utente autore) {
        log.info("Creazione Ingrediente da DTO");
        Ingrediente i = mapper.toEntity(dto);
        i.setUtente(autore);
        if (dto.categoria() != null && dto.categoria().getId() != null) {
            Categoria cat = categoriaService.getByIdOrNull(dto.categoria().getId());
            if (cat != null)
                i.setCategoria(cat);
        }
        getRepository().save(i);
        return true;
    }

    @Transactional
    public boolean updateFromDto(Long id, IngredienteDTO dto) {
        log.info("Aggiornamento Ingrediente da DTO");
        Ingrediente i = getByIdOrNull(id);
        if (i == null) {
            return false;
        }
        mapper.updateEntity(dto, i);
        if (dto.categoria() != null && dto.categoria().getId() != null) {
            Categoria cat = categoriaService.getByIdOrNull(dto.categoria().getId());
            if (cat != null)
                i.setCategoria(cat);
        }
        getRepository().save(i);
        return true;
    }

    public boolean delete(Long id) {
        log.info("Rimozione Ingrediente");
        Ingrediente i = getByIdOrNull(id);
        if (i == null) {
            return false;
        }
        deleteById(id);
        return true;
    }

    public IngredienteDTO getDTOById(Long id) {
        Ingrediente i = getByIdOrNull(id);
        return i == null ? null : mapper.toDTO(i);
    }

    public List<Ingrediente> getByUtente(Long utenteId) {
        return getRepository().findByUtenteId(utenteId);
    }
}