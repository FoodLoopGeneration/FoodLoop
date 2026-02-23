package com.generation.foodloop.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
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

    @InjectMocks
    private IngredienteService service;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(service, "repository", repository);
    }

    @Test
    @DisplayName("TC-01: Creazione ingrediente con successo e associazione categoria")
    void createFromDto_SuccessWithCategory() {
        IngredienteDTO dto = mock(IngredienteDTO.class);
        Categoria cat = new Categoria();
        cat.setId(5L);
        Ingrediente entity = new Ingrediente();
        Utente autore = new Utente();

        when(dto.categoria()).thenReturn(cat);
        when(mapper.toEntity(dto)).thenReturn(entity);
        when(categoriaService.getByIdOrNull(5L)).thenReturn(cat);

        boolean result = service.createFromDto(dto, autore);

        assertThat(result).isTrue();
        assertThat(entity.getUtente()).isEqualTo(autore);
        assertThat(entity.getCategoria()).isEqualTo(cat);
        verify(repository).save(entity);
    }

    @Test
    @DisplayName("TC-02: Validazione creazione - Errore se il nome è nullo")
    void uniqueErrorsForCreate_NullName() {
        IngredienteDTO dto = mock(IngredienteDTO.class);
        when(dto.nome()).thenReturn(null);

        Map<String, String> errors = service.uniqueErrorsForCreate(dto);

        assertThat(errors).containsKey("nome");
        assertThat(errors.get("nome")).isEqualTo("Il campo nome non può essere vuoto");
    }

    @Test
    @DisplayName("TC-03: Validazione creazione - Successo se il nome è presente")
    void uniqueErrorsForCreate_Valid() {
        IngredienteDTO dto = mock(IngredienteDTO.class);
        when(dto.nome()).thenReturn("Pasta");

        Map<String, String> errors = service.uniqueErrorsForCreate(dto);

        assertThat(errors).isEmpty();
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
    @DisplayName("TC-06: Validazione update - Errore se il nome è nullo")
    void uniqueErrorsForUpdate_NullName() {
        IngredienteDTO dto = mock(IngredienteDTO.class);
        when(dto.nome()).thenReturn(null);

        Map<String, String> errors = service.uniqueErrorsForUpdate(1L, dto);

        assertThat(errors).containsKey("nome");
        assertThat(errors.get("nome")).isEqualTo("Il campo nome non può essere vuoto");
    }

    @Test
    @DisplayName("TC-07: Cancellazione con successo (Verifica check esistenza)")
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

    @Test
    @DisplayName("TC-11: Recupero di tutti gli ingredienti")
    void getAll_ReturnsList() {
        when(repository.findAll()).thenReturn(List.of(new Ingrediente()));
        
        List<Ingrediente> result = service.getAll();
        
        assertThat(result).isNotEmpty();
        verify(repository).findAll();
    }
}
