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

import com.generation.foodloop.dto.CategoriaDTO;
import com.generation.foodloop.entities.Categoria;
import com.generation.foodloop.entities.Utente;
import com.generation.foodloop.repositories.CategoriaRepository;
import com.generation.foodloop.utils.CategoriaMapper;

@ExtendWith(MockitoExtension.class)
class CategoriaServiceTest {

    @Mock 
    private CategoriaRepository repository;

    @Mock 
    private CategoriaMapper mapper;

    @InjectMocks
    private CategoriaService service;

    @BeforeEach
    void setUp() {
        // Iniezione manuale per garantire la visibilità nella superclasse GenericService
        ReflectionTestUtils.setField(service, "repository", repository);
    }

    @Test
    @DisplayName("TC01 - Creazione categoria con successo e associazione autore")
    void createFromDto_Success() {
        CategoriaDTO dto = new CategoriaDTO(null, "Primi", null);
        Categoria entity = new Categoria();
        Utente autore = new Utente();
        when(mapper.toEntity(dto)).thenReturn(entity);

        boolean result = service.createFromDto(dto, autore);

        assertThat(result).isTrue();
        assertThat(entity.getUtente()).isEqualTo(autore);
        verify(repository).save(entity);
    }

    @Test
    @DisplayName("TC02 - Errore validazione: nome nullo in creazione")
    void uniqueErrorsForCreate_NullName() {
        CategoriaDTO dto = new CategoriaDTO(null, null, null);
        
        Map<String, String> errors = service.uniqueErrorsForCreate(dto);

        assertThat(errors).containsKey("nome");
        assertThat(errors.get("nome")).isEqualTo("il campo nome non può essere vuoto");
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
        when(repository.findById(id)).thenReturn(Optional.empty());

        boolean result = service.updateFromDto(id, CategoriaDTO.empty());

        assertThat(result).isFalse();
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("TC05 - Errore univocità: nome duplicato in fase di update (Case Insensitive)")
    void uniqueErrorsForUpdate_Conflict() {
        Long id = 1L;
        CategoriaDTO dto = new CategoriaDTO(id, "  Dolci  ", null);
        // Il service normalizza in "DOLCI" prima del check
        when(repository.existsByNomeAndId("DOLCI", id)).thenReturn(true);

        Map<String, String> errors = service.uniqueErrorsForUpdate(id, dto);

        assertThat(errors).containsKey("nome");
        assertThat(errors.get("nome")).isEqualTo("Nome già presente");
    }

    @Test
    @DisplayName("TC06 - Cancellazione categoria esistente (con check GenericService)")
    void delete_Success() {
        Long id = 1L;
        Categoria c = new Categoria();
        // Stub per getByIdOrNull
        when(repository.findById(id)).thenReturn(Optional.of(c));
        // Stub richiesto dal controllo interno di GenericService.deleteById
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
    @DisplayName("TC08 - Recupero DTO per ID con mappatura")
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

    @Test
    @DisplayName("TC09 - Ricerca categorie per Utente")
    void getByUtente_Success() {
        Long utenteId = 10L;
        when(repository.findByUtenteId(utenteId)).thenReturn(List.of(new Categoria()));

        List<Categoria> result = service.getByUtente(utenteId);

        assertThat(result).isNotEmpty();
        verify(repository).findByUtenteId(utenteId);
    }
}