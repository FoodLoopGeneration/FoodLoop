package com.generation.foodloop.services;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.generation.foodloop.repositories.UtenteRepository;

@ExtendWith(MockitoExtension.class)
class UtenteServiceTest {

    @Mock private UtenteRepository repository;
    @InjectMocks private UtenteService service;

    @Test
    @DisplayName("TC01 - Eccezione se l'utente con ingredienti non esiste")
    void getByIdWithIngredienti_Throws() {
        when(repository.findWithIngredientiById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getByIdWithIngredienti(1L))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Utente non trovato");
    }

}