package com.generation.foodloop.services;

import java.util.Optional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.generation.foodloop.dto.UtenteDTO;
import com.generation.foodloop.entities.Utente;
import com.generation.foodloop.repositories.UtenteRepository;
import com.generation.foodloop.utils.UtenteMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UtenteService extends GenericService<Long, Utente, UtenteRepository> {

    private final UtenteMapper mapper;
    private final BCryptPasswordEncoder passwordEncoder;

    public Utente getByIdWithIngredienti(Long id) {
        return getRepository().findWithIngredientiById(id)
                .orElseThrow(() -> new RuntimeException("Utente non trovato con ID: " + id));
    }

    public Optional<Utente> findById(Long id) {
        return getRepository().findById(id);
    }

    public Optional<Utente> findByEmail(String email) {
        return getRepository().findByEmail(email);
    }

    @Transactional
    public boolean createFromDto(UtenteDTO dto) {
        log.info("Tentativo di creazione utente per email: {}", dto.email());
        
        if (getRepository().findByEmail(dto.email()).isPresent()) {
            log.warn("Registrazione fallita: email {} già presente", dto.email());
            return false;
        }

        Utente u = mapper.toEntity(dto);
        
        u.setPassword(passwordEncoder.encode(dto.password()));
        
        getRepository().save(u);
        log.info("Utente registrato con successo: {}", u.getEmail());
        return true;
    }
}