package com.generation.foodloop.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.generation.foodloop.dto.RicettaDTO;
import com.generation.foodloop.entities.Ricetta;
import com.generation.foodloop.repositories.RicettaRepository;
import com.generation.foodloop.utils.RicettaMapper;

@ExtendWith(MockitoExtension.class)
@DisplayName("Unit Test per RicettaService")
class RicettaServiceTest {

    @Mock
    private RicettaRepository repository;

    @Mock
    private RicettaMapper mapper;

    private RicettaService service;

    @BeforeEach
    void setUp() {
        service = new RicettaService(mapper);
        org.springframework.test.util.ReflectionTestUtils.setField(service, "repository", repository);
    }

    @Test
    @DisplayName("TC-01: Creazione con nome duplicato deve restituire errore")
    void uniqueErrorsForCreate_NomeEsistente_RitornaErrore() {
        RicettaDTO dto = mock(RicettaDTO.class);
        when(dto.nome()).thenReturn("Pasta al Forno");
        when(repository.existsByNome("PASTA AL FORNO")).thenReturn(true);

        Map<String, String> errors = service.uniqueErrorsForCreate(dto);

        assertThat(errors).containsKey("nome");
        assertThat(errors.get("nome")).isEqualTo("Nome già presente");
    }

    @Test
    @DisplayName("TC-02: Salvataggio da DTO deve invocare il repository")
    void createFromDto_Valido_SalvataggioRiuscito() {
        RicettaDTO dto = mock(RicettaDTO.class);
        Ricetta entita = new Ricetta();
        when(mapper.toEntity(dto)).thenReturn(entita);

        boolean result = service.createFromDto(dto);

        assertThat(result).isTrue();
        verify(repository).save(entita);
    }

    @Test
    @DisplayName("TC-03: Aggiornamento con ID inesistente deve fallire")
    void updateFromDto_IdInesistente_RitornaFalse() {
        Long id = 99L;
        RicettaDTO dto = mock(RicettaDTO.class);
        when(repository.findById(id)).thenReturn(Optional.empty());

        boolean result = service.updateFromDto(id, dto);

        assertThat(result).isFalse();
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("TC-04: Controllo univocità in aggiornamento (nome già usato da altri)")
    void uniqueErrorsForUpdate_NomeOccupatoDaAltroID_RitornaErrore() {
        Long idCorrente = 1L;
        RicettaDTO dto = mock(RicettaDTO.class);
        when(dto.nome()).thenReturn("Pizza");
        when(repository.existsByNomeAndIdNot("PIZZA", idCorrente)).thenReturn(true);

        Map<String, String> errors = service.uniqueErrorsForUpdate(idCorrente, dto);

        assertThat(errors).containsKey("nome");
    }

    @Test
    @DisplayName("TC-05: Cancellazione di ricetta esistente")
    void delete_Esistente_RitornaTrue() {
        Long id = 1L;
        when(repository.existsById(id)).thenReturn(true);
        when(repository.findById(id)).thenReturn(Optional.of(new Ricetta()));

        boolean result = service.delete(id);

        assertThat(result).isTrue();
        verify(repository).deleteById(id);
    }

    @Test
    @DisplayName("TC-06: Verifica appartenenza ricetta all'utente")
    void belongsToUser_CheckCorretto() {
        Long rId = 1L;
        Long uId = 10L;
        when(repository.existsByIdAndUtenteId(rId, uId)).thenReturn(true);

        assertThat(service.belongsToUser(rId, uId)).isTrue();
    }

    @Test
    @DisplayName("TC-07: Recupero DTO per ID")
    void getDTOById_RitornaDtoMappato() {
        Long id = 1L;
        Ricetta r = new Ricetta();
        RicettaDTO dto = mock(RicettaDTO.class);
        when(repository.findById(id)).thenReturn(Optional.of(r));
        when(mapper.toDTO(r)).thenReturn(dto);

        RicettaDTO result = service.getDTOById(id);

        assertThat(result).isEqualTo(dto);
    }
}