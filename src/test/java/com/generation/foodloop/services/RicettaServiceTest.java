package com.generation.foodloop.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

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

    @Mock
    private FileStorageService fileStorageService;

    private RicettaService service;

    @BeforeEach
    void setUp() {
        service = new RicettaService(mapper, fileStorageService);
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
    @DisplayName("TC-02: Creazione ricetta con salvataggio foto")
    void createFromDto_ConFoto_SalvaCorrettamente() throws IOException {
        RicettaDTO dto = mock(RicettaDTO.class);
        Ricetta r = new Ricetta();
        MultipartFile fotoMock = mock(MultipartFile.class);

        when(dto.foto()).thenReturn(fotoMock);
        when(mapper.toEntity(dto)).thenReturn(r);
        when(fileStorageService.save(fotoMock)).thenReturn("foto_generata.jpg");

        boolean result = service.createFromDto(dto);

        assertThat(result).isTrue();
        assertThat(r.getFoto()).isEqualTo("foto_generata.jpg");
        verify(repository).save(r);
    }

    @Test
    @DisplayName("TC-03: Update ricetta con nuova foto")
    void updateFromDto_NuovaFoto_AggiornaFile() throws IOException {
        Long id = 1L;
        RicettaDTO dto = mock(RicettaDTO.class);
        Ricetta r = new Ricetta();
        MultipartFile nuovaFoto = mock(MultipartFile.class);

        when(repository.findById(id)).thenReturn(Optional.of(r));
        when(dto.foto()).thenReturn(nuovaFoto);
        when(nuovaFoto.isEmpty()).thenReturn(false);
        when(fileStorageService.save(nuovaFoto)).thenReturn("nuova_foto.png");

        boolean result = service.updateFromDto(id, dto);

        assertThat(result).isTrue();
        assertThat(r.getFoto()).isEqualTo("nuova_foto.png");
        verify(mapper).updateEntity(dto, r);
        verify(repository).save(r);
    }

    @Test
    @DisplayName("TC-04: Errore univocità in fase di update")
    void uniqueErrorsForUpdate_NomeGiaUsato_RitornaErrore() {
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
        Ricetta r = new Ricetta();
        when(repository.findById(id)).thenReturn(Optional.of(r));
        when(repository.existsById(id)).thenReturn(true);

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
}