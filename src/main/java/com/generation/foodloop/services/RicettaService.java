package com.generation.foodloop.services;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.generation.foodloop.dto.RicettaDTO;
import com.generation.foodloop.entities.Ricetta;
import com.generation.foodloop.repositories.RicettaRepository;
import com.generation.foodloop.utils.RicettaMapper;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Slf4j
public class RicettaService extends GenericService<Long, Ricetta, RicettaRepository> {

    private final RicettaMapper mapper;
    private final FileStorageService fileStorageService;

    private String normNome(String nome) {
        return nome == null ? null : nome.trim().toUpperCase();
    }

    public Map<String, String> uniqueErrorsForCreate(RicettaDTO dto) {
        Map<String, String> errors = new HashMap<>();
        String nome = normNome(dto.nome());
        if (nome != null && getRepository().existsByNome(nome)) {
            errors.put("nome", "Nome già presente");
        }
        return errors;
    }

    public Map<String, String> uniqueErrorsForUpdate(Long id, RicettaDTO dto) {
        Map<String, String> errors = new HashMap<>();
        String nome = normNome(dto.nome());
        if (nome != null && getRepository().existsByNomeAndIdNot(nome, id)) {
            errors.put("nome", "Nome già presente in un'altra ricetta");
        }
        return errors;
    }

    @Transactional
    public boolean createFromDto(RicettaDTO dto) {
        log.info("Creazione Ricetta da DTO");
        Ricetta r = mapper.toEntity(dto);
        
        try {
            String fileName = fileStorageService.save(dto.foto());
            r.setFoto(fileName);
        } catch (IOException e) {
            log.warn("Errore nel salvataggio dell'immagine");
            throw new RuntimeException("Errore nel salvataggio dell'immagine: " + e.getMessage());
        }
        
        getRepository().save(r);
        return true;
    }

    @Transactional
    public boolean updateFromDto(Long id, RicettaDTO dto) {
        log.info("Aggiornamento Ricetta da DTO");
        Ricetta r = getByIdOrNull(id);
        if (r == null) {
            return false;
        }
        
        mapper.updateEntity(dto, r);
        
        if (dto.foto() != null && !dto.foto().isEmpty()) {
            try {
                String fileName = fileStorageService.save(dto.foto());
                r.setFoto(fileName);
            } catch (IOException e) {
                log.warn("Errore nell'aggiornamento dell'immagine");
                throw new RuntimeException("Errore nell'aggiornamento dell'immagine: " + e.getMessage());
            }
        }
        
        getRepository().save(r);
        return true;
    }

    public boolean delete(Long id) {
        Ricetta r = getByIdOrNull(id);
        if (r == null) {
            return false;
        }
        deleteById(id);
        return true;
    }

    public RicettaDTO getDTOById(Long id) {
        Ricetta r = getByIdOrNull(id);
        return r == null ? null : mapper.toDTO(r);
    }

    public boolean belongsToUser(Long ricettaId, Long utenteId) {
        if (ricettaId == null || utenteId == null) return false;
        return getRepository().existsByIdAndUtenteId(ricettaId, utenteId);
    }

    public List<Ricetta> getAll() {
        return getRepository().findAll();
    }

    public List<Ricetta> getByUtente(Long utenteId) {
        return getRepository().findByUtenteId(utenteId);
    }

    public Ricetta getByIdWithIngredienti(Long id) {
        return getRepository().findWithIngredientiById(id).orElse(null);
    }
}