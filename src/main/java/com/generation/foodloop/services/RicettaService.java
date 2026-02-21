package com.generation.foodloop.services;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.generation.foodloop.dto.RicettaDTO;
import com.generation.foodloop.entities.Ricetta;
import com.generation.foodloop.entities.Ingrediente;
import com.generation.foodloop.entities.Utente;
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
    private final IngredienteService ingredienteService;

    private String normNome(String nome) {
        return nome == null ? null : nome.trim().toUpperCase();
    }

    private Set<Ingrediente> parseIngredienti(String idsString) {
        if (idsString == null || idsString.isBlank()) {
            return new HashSet<>();
        }

        return Arrays.stream(idsString.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(Long::valueOf)
                .map(ingredienteService::getByIdOrNull)
                .filter(ing -> ing != null)
                .collect(Collectors.toSet());
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
    public boolean createFromDto(RicettaDTO dto, Utente autore) {
        log.info("Creazione Ricetta da DTO con stringa ingredienti: {}", dto.ingredienti());
        
        Ricetta r = mapper.toEntity(dto);
        r.setUtente(autore);
        
        r.setIngredienti(parseIngredienti(dto.ingredienti()));

        try {
            String fileName = fileStorageService.save(dto.foto());
            r.setFoto(fileName);
        } catch (IOException e) {
            log.error("Errore nel salvataggio dell'immagine", e);
            throw new RuntimeException("Errore nel salvataggio dell'immagine: " + e.getMessage());
        }

        getRepository().save(r);
        return true;
    }

    @Transactional
    public boolean updateFromDto(Long id, RicettaDTO dto) {
        log.info("Aggiornamento Ricetta ID {} da DTO", id);
        
        Ricetta r = getByIdOrNull(id);
        if (r == null) {
            return false;
        }

        mapper.updateEntity(dto, r);

        r.getIngredienti().clear();
        r.getIngredienti().addAll(parseIngredienti(dto.ingredienti()));

        if (dto.foto() != null && !dto.foto().isEmpty()) {
            try {
                String fileName = fileStorageService.save(dto.foto());
                r.setFoto(fileName);
            } catch (IOException e) {
                log.error("Errore nell'aggiornamento dell'immagine", e);
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
        if (ricettaId == null || utenteId == null)
            return false;
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

    public List<Ricetta> getSuggerimenti(Long utenteId) {
        List<Ingrediente> dispensa = ingredienteService.getByUtente(utenteId);

        Set<String> nomiInDispensa = dispensa.stream()
                .map(i -> i.getNome().toUpperCase().trim())
                .collect(Collectors.toSet());

        return getRepository().findAll().stream()
                .filter(ricetta -> ricetta.getIngredienti().stream()
                        .allMatch(ing -> nomiInDispensa.contains(ing.getNome().toUpperCase().trim())))
                .toList();
    }
}