package com.generation.foodloop.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileStorageService {

    private final String uploadDir = "uploads/";

    public String save(MultipartFile file) throws IOException {

        if (file == null || file.isEmpty())
            return null;

        String filename = UUID.randomUUID()
                + "-" + file.getOriginalFilename();

        Path path = Paths.get(uploadDir + filename);

        Files.createDirectories(path.getParent());
        Files.write(path, file.getBytes());

        return filename;
    }
}
