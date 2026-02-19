package com.generation.foodloop.services;

import java.util.Optional;
import org.springframework.stereotype.Service;

import com.generation.foodloop.dto.UtenteDTO;
import com.generation.foodloop.entities.Utente;
import com.generation.foodloop.repositories.UtenteRepository;
import com.generation.foodloop.utils.UtenteMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UtenteService extends GenericService<Long, Utente, UtenteRepository>{

    private final UtenteMapper mapper;

    public Utente getByIdWithIngredienti(Long id) {
        return getRepository().findWithIngredientiById(id)
                .orElseThrow(() -> new RuntimeException("Utente non trovato con ID: " + id));
    }

    public Optional<Utente> findById(Long id) {
        return getRepository().findById(id);
    }

    public Optional<Utente> findUtenteByEmailAndPassword(String email, String password) {
        log.info("La password è {}, la mail è {}", password, email);
        return getRepository().findByEmailAndPassword(email, password);
    }
    

    public Optional<Utente> findByEmail(String email) {
        return getRepository().findByEmail(email);
    }

    public boolean createFromDto(UtenteDTO dto) {
        log.info("Creazione utente da DTO");
        Utente u = mapper.toEntity(dto);
        getRepository().save(u);
        return true;
    }
}