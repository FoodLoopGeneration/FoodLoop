package com.generation.foodloop.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.generation.foodloop.dto.IngredienteDTO;
import com.generation.foodloop.entities.Categoria;
import com.generation.foodloop.entities.Ingrediente;
import com.generation.foodloop.entities.Utente;
import com.generation.foodloop.repositories.IngredienteRepository;
import com.generation.foodloop.utils.IngredienteMapper;

@ExtendWith(MockitoExtension.class)
@DisplayName("Unit Test per IngredienteService")
class IngredienteServiceTest {

    @Mock
    private IngredienteRepository repository;
    @Mock
    private IngredienteMapper mapper;
    @Mock
    private CategoriaService categoriaService;

    private IngredienteService service;

    @BeforeEach
    void setUp() {
        service = new IngredienteService(mapper, categoriaService);
        ReflectionTestUtils.setField(service, "repository", repository);
    }

    @Test
    @DisplayName("TC-01: Creazione ingrediente con successo e associazione categoria")
    void createFromDto_SuccessWithCategory() {
        IngredienteDTO dto = mock(IngredienteDTO.class);
        Categoria catDto = new Categoria();
        catDto.setId(5L);
        Ingrediente entity = new Ingrediente();

        when(dto.categoria()).thenReturn(catDto);
        when(mapper.toEntity(dto)).thenReturn(entity);
        when(categoriaService.getByIdOrNull(5L)).thenReturn(catDto);

        boolean result = service.createFromDto(dto, new Utente());

        assertThat(result).isTrue();
        assertThat(entity.getCategoria()).isEqualTo(catDto);
        verify(repository).save(entity);
    }

    @Test
    @DisplayName("TC-02: Errore univocità - Nome già presente in fase di creazione")
    void uniqueErrorsForCreate_Duplicate() {
        IngredienteDTO dto = mock(IngredienteDTO.class);
        when(dto.nome()).thenReturn("Sale");
        when(repository.existsByNome("SALE")).thenReturn(true);

        Map<String, String> errors = service.uniqueErrorsForCreate(dto);

        assertThat(errors).containsKey("nome");
        assertThat(errors.get("nome")).isEqualTo("Ingrediente già presente in archivio");
    }

    @Test
    @DisplayName("TC-04: Update ingrediente esistente con successo")
    void updateFromDto_Success() {
        Long id = 1L;
        IngredienteDTO dto = mock(IngredienteDTO.class);
        Ingrediente existingEntity = new Ingrediente();

        when(repository.findById(id)).thenReturn(Optional.of(existingEntity));
        lenient().when(dto.categoria()).thenReturn(null);

        boolean result = service.updateFromDto(id, dto);

        assertThat(result).isTrue();
        verify(mapper).updateEntity(dto, existingEntity);
        verify(repository).save(existingEntity);
    }

    @Test
    @DisplayName("TC-05: Update fallisce se l'ingrediente non esiste")
    void updateFromDto_NotFound_ReturnsFalse() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        boolean result = service.updateFromDto(99L, mock(IngredienteDTO.class));

        assertThat(result).isFalse();
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("TC-06: Errore univocità - Nome occupato da un altro ID in fase di update")
    void uniqueErrorsForUpdate_Conflict() {
        Long id = 1L;
        IngredienteDTO dto = mock(IngredienteDTO.class);
        when(dto.nome()).thenReturn("Pepe");
        when(repository.existsByNomeAndIdNot("PEPE", id)).thenReturn(true);

        Map<String, String> errors = service.uniqueErrorsForUpdate(id, dto);

        assertThat(errors).containsKey("nome");
        assertThat(errors.get("nome")).contains("già utilizzato");
    }

    @Test
    @DisplayName("TC-07: Cancellazione con successo")
    void delete_Existing_ReturnsTrue() {
        Long id = 1L;
        Ingrediente i = new Ingrediente();
        when(repository.findById(id)).thenReturn(Optional.of(i));
        when(repository.existsById(id)).thenReturn(true);

        boolean result = service.delete(id);

        assertThat(result).isTrue();
        verify(repository).deleteById(id);
    }

    @Test
    @DisplayName("TC-08: Cancellazione fallisce se l'ingrediente non esiste")
    void delete_NotExisting_ReturnsFalse() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        boolean result = service.delete(1L);

        assertThat(result).isFalse();
        verify(repository, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("TC-09: Ricerca ingredienti per utente")
    void getByUtente_ReturnsList() {
        Long utenteId = 10L;
        List<Ingrediente> lista = List.of(new Ingrediente(), new Ingrediente());
        when(repository.findByUtenteId(utenteId)).thenReturn(lista);

        List<Ingrediente> result = service.getByUtente(utenteId);

        assertThat(result).hasSize(2);
        verify(repository).findByUtenteId(utenteId);
    }

    @Test
    @DisplayName("TC-10: Conversione DTO per ID esistente")
    void getDTOById_ReturnsPopulatedDto() {
        Long id = 1L;
        Ingrediente i = new Ingrediente();
        IngredienteDTO dto = mock(IngredienteDTO.class);

        when(repository.findById(id)).thenReturn(Optional.of(i));
        when(mapper.toDTO(i)).thenReturn(dto);

        IngredienteDTO result = service.getDTOById(id);

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(dto);
    }
}