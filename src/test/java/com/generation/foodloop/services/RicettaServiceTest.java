package com.generation.foodloop.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

import com.generation.foodloop.dto.RicettaDTO;
import com.generation.foodloop.entities.Ingrediente;
import com.generation.foodloop.entities.Ricetta;
import com.generation.foodloop.entities.Utente;
import com.generation.foodloop.repositories.RicettaRepository;
import com.generation.foodloop.utils.RicettaMapper;

@ExtendWith(MockitoExtension.class)
@DisplayName("Unit Test per RicettaService")
class RicettaServiceTest {

    @Mock private RicettaRepository repository;
    @Mock private RicettaMapper mapper;
    @Mock private FileStorageService fileStorageService;
    @Mock private IngredienteService ingredienteService;

    private RicettaService service;

    @BeforeEach
    void setUp() {
        service = new RicettaService(mapper, fileStorageService, ingredienteService);
        ReflectionTestUtils.setField(service, "repository", repository);
    }

    @Test
    @DisplayName("TC-01: Creazione con nome duplicato deve restituire errore")
    void uniqueErrorsForCreate_NomeEsistente_RitornaErrore() {
        RicettaDTO dto = mock(RicettaDTO.class);
        when(dto.nome()).thenReturn("Pasta");
        when(repository.existsByNome("PASTA")).thenReturn(true);

        Map<String, String> errors = service.uniqueErrorsForCreate(dto);
        assertThat(errors).containsKey("nome");
    }

    @Test
    @DisplayName("TC-02: Creazione ricetta con salvataggio foto")
    void createFromDto_ConFoto_SalvaCorrettamente() throws IOException {
        RicettaDTO dto = mock(RicettaDTO.class);
        MultipartFile foto = mock(MultipartFile.class);
        Ricetta r = new Ricetta();

        lenient().when(dto.foto()).thenReturn(foto);
        lenient().when(foto.isEmpty()).thenReturn(false);
        lenient().when(mapper.toEntity(dto)).thenReturn(r);
        lenient().when(fileStorageService.save(foto)).thenReturn("foto.png");

        boolean result = service.createFromDto(dto, new Utente());
        assertThat(result).isTrue();
        verify(repository, atLeastOnce()).save(any());
    }

    @Test
    @DisplayName("TC-03: Creazione fallisce se il caricamento immagine lancia eccezione")
    void createFromDto_ImageUploadFails_ThrowsException() throws IOException {
        RicettaDTO dto = mock(RicettaDTO.class);
        MultipartFile foto = mock(MultipartFile.class);
        
        lenient().when(dto.foto()).thenReturn(foto);
        lenient().when(foto.isEmpty()).thenReturn(false);
        lenient().when(mapper.toEntity(dto)).thenReturn(new Ricetta());
        lenient().when(fileStorageService.save(foto)).thenThrow(new IOException("Disk Error"));

        assertThatThrownBy(() -> service.createFromDto(dto, new Utente()))
            .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("TC-04: Update ricetta con nuova foto")
    void updateFromDto_NuovaFoto_AggiornaFile() throws IOException {
        Long id = 1L;
        Ricetta r = new Ricetta();
        RicettaDTO dto = mock(RicettaDTO.class);
        MultipartFile foto = mock(MultipartFile.class);

        when(repository.findById(id)).thenReturn(Optional.of(r));
        lenient().when(dto.foto()).thenReturn(foto);
        lenient().when(foto.isEmpty()).thenReturn(false);
        lenient().when(fileStorageService.save(foto)).thenReturn("new.png");

        boolean result = service.updateFromDto(id, dto);
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("TC-05: Errore univocità in fase di update")
    void uniqueErrorsForUpdate_NomeGiaUsato_RitornaErrore() {
        RicettaDTO dto = mock(RicettaDTO.class);
        when(dto.nome()).thenReturn("Pizza");
        when(repository.existsByNomeAndIdNot("PIZZA", 1L)).thenReturn(true);

        Map<String, String> errors = service.uniqueErrorsForUpdate(1L, dto);
        assertThat(errors).containsKey("nome");
    }

    @Test
    @DisplayName("TC-06: Update fallisce se la ricetta non esiste")
    void updateFromDto_NonEsistente_RitornaFalse() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        boolean result = service.updateFromDto(99L, mock(RicettaDTO.class));
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("TC-07: Cancellazione di ricetta esistente")
    void delete_Esistente_RitornaTrue() {
        when(repository.findById(1L)).thenReturn(Optional.of(new Ricetta()));
        when(repository.existsById(1L)).thenReturn(true);
        boolean result = service.delete(1L);
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("TC-08: Verifica appartenenza ricetta all'utente")
    void belongsToUser_CheckCorretto() {
        when(repository.existsByIdAndUtenteId(1L, 10L)).thenReturn(true);
        assertThat(service.belongsToUser(1L, 10L)).isTrue();
    }

    @Test
    @DisplayName("TC-09: Suggerimenti - Match parziale degli ingredienti")
    void getSuggerimenti_MatchParziale_RitornaRicetta() {
        Ricetta r = new Ricetta();
        Ingrediente i = new Ingrediente(); 
        i.setNome("Zucchero"); 
        r.setIngredienti(new HashSet<>(List.of(i)));

        when(repository.findAll()).thenReturn(List.of(r));
        when(ingredienteService.getByUtente(1L)).thenReturn(List.of(i));

        assertThat(service.getSuggerimenti(1L)).hasSize(1);
    }

    @Test
    @DisplayName("TC-10: Suggerimenti - Lista vuota se mancano ingredienti")
    void getSuggerimenti_MancaIngrediente_RitornaListaVuota() {
        Ricetta r = new Ricetta();
        Ingrediente iNecessario = new Ingrediente();
        iNecessario.setNome("Uova");
        r.setIngredienti(new HashSet<>(List.of(iNecessario)));

        when(repository.findAll()).thenReturn(List.of(r));
        when(ingredienteService.getByUtente(1L)).thenReturn(Collections.emptyList());

        List<Ricetta> result = service.getSuggerimenti(1L);
        assertThat(result).isEmpty();
    }
}