package com.generation.foodloop.services;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import com.generation.foodloop.dto.UtenteDTO;
import com.generation.foodloop.entities.Ruolo;
import com.generation.foodloop.entities.Utente;
import com.generation.foodloop.repositories.RuoloRepository;
import com.generation.foodloop.repositories.UtenteRepository;
import com.generation.foodloop.utils.UtenteMapper;

@ExtendWith(MockitoExtension.class)
class UtenteServiceTest {

    @Mock
    private UtenteRepository repository;
    @Mock
    private RuoloRepository ruoloRepository;
    @Mock
    private UtenteMapper mapper;
    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UtenteService service;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(service, "repository", repository);
    }

    @Test
    @DisplayName("TC01 - Eccezione se l'utente con ingredienti non esiste")
    void getByIdWithIngredienti_Throws() {
        when(repository.findWithIngredientiById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getByIdWithIngredienti(1L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Utente non trovato");
    }

    @Test
    @DisplayName("TC02 - Successo creazione utente con hashing password e ruolo USR")
    void createFromDto_Success() {
        UtenteDTO dto = new UtenteDTO(null, null, null, "Mario", "Rossi", "mario@test.com", "password123");
        Utente entity = new Utente();
        Ruolo ruoloUsr = new Ruolo();
        ruoloUsr.setNome("USR");

        when(repository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(mapper.toEntity(dto)).thenReturn(entity);
        when(passwordEncoder.encode("password123")).thenReturn("hashed_pass");
        when(ruoloRepository.findByNome("USR")).thenReturn(Optional.of(ruoloUsr));
        when(repository.save(any(Utente.class))).thenReturn(entity);

        boolean result = service.createFromDto(dto);

        assertThat(result).isTrue();
        verify(repository).save(entity);
    }

    @Test
    @DisplayName("TC03 - Fallimento se l'email è già presente nel database")
    void createFromDto_EmailDuplicate_ReturnsFalse() {
        UtenteDTO dto = new UtenteDTO(null, null, null, "Mario", "Rossi", "esistente@test.com", "pass");
        when(repository.findByEmail("esistente@test.com")).thenReturn(Optional.of(new Utente()));

        boolean result = service.createFromDto(dto);

        assertThat(result).isFalse();
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("TC04 - Fallimento se il ruolo USR non esiste (Errore configurazione DB)")
    void createFromDto_RoleNotFound_ReturnsFalse() {

        UtenteDTO dto = new UtenteDTO(null, null, null, "Mario", "Rossi", "mario@test.com", "pass");

        when(repository.findByEmail(anyString())).thenReturn(Optional.empty());

        when(mapper.toEntity(dto)).thenReturn(new Utente());

        when(ruoloRepository.findByNome("USR")).thenReturn(Optional.empty());

        boolean result = service.createFromDto(dto);

        assertThat(result).isFalse();

        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("TC05 - Gestione generica eccezioni durante il salvataggio")
    void createFromDto_GenericError_ReturnsFalse() {
        UtenteDTO dto = new UtenteDTO(null, null, null, "Mario", "Rossi", "mario@test.com", "pass");

        when(repository.findByEmail(anyString())).thenReturn(Optional.empty());

        when(mapper.toEntity(any())).thenThrow(new RuntimeException("Simulated Error"));

        boolean result = service.createFromDto(dto);

        assertThat(result).isFalse();
    }
}