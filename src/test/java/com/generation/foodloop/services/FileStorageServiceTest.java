package com.generation.foodloop.services;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

class FileStorageServiceTest {

    private final FileStorageService service = new FileStorageService();

    @Test
    @DisplayName("TC01 - Salvataggio file valido con generazione UUID")
    void saveFile_Success() throws IOException {
        MockMultipartFile file = new MockMultipartFile(
            "foto", "test.png", "image/png", "data".getBytes()
        );

        String filename = service.save(file);

        assertThat(filename).isNotNull();
        assertThat(filename).contains("test.png");
        Files.deleteIfExists(Paths.get("uploads/" + filename));
    }

    @Test
    @DisplayName("TC02 - Gestione file nullo o vuoto")
    void saveFile_Empty() throws IOException {
        assertThat(service.save(null)).isNull();
        
        MockMultipartFile emptyFile = new MockMultipartFile("foto", new byte[0]);
        assertThat(service.save(emptyFile)).isNull();
    }
}