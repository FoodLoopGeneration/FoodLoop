package com.generation.foodloop.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.generation.foodloop.dto.IngredienteDTO;
import com.generation.foodloop.repositories.IngredienteRepository;
import com.generation.foodloop.utils.IngredienteMapper;

@ExtendWith(MockitoExtension.class)
class IngredienteServiceTest {

    @Mock private IngredienteRepository repository;
    @Mock private IngredienteMapper mapper;
    @Mock private com.generation.foodloop.services.CategoriaService categoriaService;
    private IngredienteService service;

    @BeforeEach
    void setUp() {
        service = new IngredienteService(mapper, categoriaService);
        ReflectionTestUtils.setField(service, "repository", repository);
    }

    @Test
    @DisplayName("TC01 - Creazione ingrediente con successo")
    void createFromDto_Success() {
        IngredienteDTO dto = IngredienteDTO.empty();
        com.generation.foodloop.entities.Ingrediente entity = new com.generation.foodloop.entities.Ingrediente();
        when(mapper.toEntity(dto)).thenReturn(entity);

        com.generation.foodloop.entities.Utente autore = new com.generation.foodloop.entities.Utente();

        boolean result = service.createFromDto(dto, autore);

        assertThat(result).isTrue();
        verify(repository).save(entity);
    }

    @Test
    @DisplayName("TC02 - Errore univocità: ingrediente già presente in archivio")
    void uniqueErrorsForCreate_Duplicate() {
        IngredienteDTO dto = new IngredienteDTO(null, "Sale", null, null, null, null, null, null);
        when(repository.existsByNome("SALE")).thenReturn(true);

        Map<String, String> errors = service.uniqueErrorsForCreate(dto);

        assertThat(errors).containsKey("nome");
        assertThat(errors.get("nome")).isEqualTo("Ingrediente già presente in archivio");
    }

    @Test
    @DisplayName("TC03 - Errore univocità update: nome occupato da altro ID")
    void uniqueErrorsForUpdate_Conflict() {
        Long id = 1L;
        IngredienteDTO dto = new IngredienteDTO(id, "Pepe", null, null, null, null, null, null);
        when(repository.existsByNomeAndIdNot("PEPE", id)).thenReturn(true);

        Map<String, String> errors = service.uniqueErrorsForUpdate(id, dto);

        assertThat(errors).containsKey("nome");
        assertThat(errors.get("nome")).contains("già utilizzato da un altro");
    }
}