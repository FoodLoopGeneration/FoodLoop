package com.generation.foodloop.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.generation.foodloop.dto.CategoriaDTO;
import com.generation.foodloop.entities.Categoria;
import com.generation.foodloop.repositories.CategoriaRepository;
import com.generation.foodloop.utils.CategoriaMapper;

@ExtendWith(MockitoExtension.class)
class CategoriaServiceTest {

    @Mock private CategoriaRepository repository;
    @Mock private CategoriaMapper mapper;
    private CategoriaService service;

    @BeforeEach
    void setUp() {
        service = new CategoriaService(mapper);
        ReflectionTestUtils.setField(service, "repository", repository);
    }

    // @Test
    // @DisplayName("TC01 - Creazione categoria con successo")
    // void createFromDto_Success() {
    //     CategoriaDTO dto = new CategoriaDTO(null, "Primi", null);
    //     Categoria entity = new Categoria();
    //     when(mapper.toEntity(dto)).thenReturn(entity);

    //     boolean result = service.createFromDto(dto);

    //     assertThat(result).isTrue();
    //     verify(repository).save(entity);
    // }

    @Test
    @DisplayName("TC02 - Errore univocità: nome già presente in creazione")
    void uniqueErrorsForCreate_Duplicate() {
        CategoriaDTO dto = new CategoriaDTO(null, "  Dolci  ", null);
        when(repository.existsByNome("DOLCI")).thenReturn(true);

        Map<String, String> errors = service.uniqueErrorsForCreate(dto);

        assertThat(errors).containsKey("nome");
        assertThat(errors.get("nome")).isEqualTo("Nome già presente");
    }

    @Test
    @DisplayName("TC03 - Aggiornamento categoria esistente")
    void updateFromDto_Success() {
        Long id = 1L;
        Categoria existing = new Categoria();
        CategoriaDTO dto = new CategoriaDTO(id, "Secondi", null);
        
        when(repository.findById(id)).thenReturn(Optional.of(existing));

        boolean result = service.updateFromDto(id, dto);

        assertThat(result).isTrue();
        verify(mapper).updateEntity(dto, existing);
        verify(repository).save(existing);
    }

    @Test
    @DisplayName("TC04 - Errore aggiornamento: categoria non trovata")
    void updateFromDto_NotFound() {
        Long id = 99L;
        CategoriaDTO dto = CategoriaDTO.empty();
        when(repository.findById(id)).thenReturn(Optional.empty());

        boolean result = service.updateFromDto(id, dto);

        assertThat(result).isFalse();
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("TC05 - Errore univocità: nome duplicato in fase di update")
    void uniqueErrorsForUpdate_Conflict() {
        Long id = 1L;
        CategoriaDTO dto = new CategoriaDTO(id, "Contorni", null);
        when(repository.existsByNomeAndId("CONTORNI", id)).thenReturn(true);

        Map<String, String> errors = service.uniqueErrorsForUpdate(id, dto);

        assertThat(errors).containsKey("nome");
        assertThat(errors.get("nome")).contains("già presente");
    }

    @Test
    @DisplayName("TC06 - Cancellazione categoria esistente")
    void delete_Success() {
        Long id = 1L;
        Categoria c = new Categoria();
        when(repository.findById(id)).thenReturn(Optional.of(c));
        when(repository.existsById(id)).thenReturn(true);

        boolean result = service.delete(id);

        assertThat(result).isTrue();
        verify(repository).deleteById(id);
    }

    @Test
    @DisplayName("TC07 - Errore cancellazione: ID inesistente")
    void delete_Fail_NotFound() {
        Long id = 500L;
        when(repository.findById(id)).thenReturn(Optional.empty());

        boolean result = service.delete(id);

        assertThat(result).isFalse();
        verify(repository, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("TC08 - Recupero DTO per ID")
    void getDTOById_Success() {
        Long id = 1L;
        Categoria entity = new Categoria();
        CategoriaDTO dto = new CategoriaDTO(id, "Test", null);
        
        when(repository.findById(id)).thenReturn(Optional.of(entity));
        when(mapper.toDTO(entity)).thenReturn(dto);

        CategoriaDTO result = service.getDTOById(id);

        assertThat(result).isNotNull();
        assertThat(result.nome()).isEqualTo("Test");
    }
}