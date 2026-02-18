package com.generation.foodloop.services;

import java.util.Optional;

import org.springframework.stereotype.Service;
import com.generation.foodloop.entities.Utente;
import com.generation.foodloop.repositories.UtenteRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UtenteService {

    private final UtenteRepository utenteRepository;

    public Utente getByIdWithIngredienti(Long id) {
        return utenteRepository.findWithIngredientiById(id)
                .orElseThrow(() -> new RuntimeException("Utente non trovato con ID: " + id));
    }

    public Optional<Utente> findById(Long id) {
        return utenteRepository.findById(id);
    }

    public Optional<Utente> findUtenteByNameAndPassword(String name, String password) {
        return utenteRepository.findByUsernameAndPassword(name, password);
    }
}