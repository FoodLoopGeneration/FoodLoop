package com.generation.foodloop.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.generation.foodloop.dto.RicettaDTO;
import com.generation.foodloop.entities.Ricetta;
import com.generation.foodloop.repositories.RicettaRepository;
import com.generation.foodloop.utils.RicettaMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class RicettaService extends GenericService<Long, Ricetta, RicettaRepository> {

    private final RicettaMapper mapper;
    
    private final String UPLOAD_DIR = "src/main/resources/static/uploads/ricette/";

    private String normNome(String nome) {
        return nome == null ? null : nome.trim().toUpperCase();
    }

    public Map<String, String> uniqueErrorsForCreate(RicettaDTO dto) {
        Map<String, String> errors = new HashMap<>();
        String nome = normNome(dto.nome());
        if (nome != null && getRepository().existsByNome(nome)) {
            errors.put("nome", "Nome già presente");
        }
        return errors;
    }

    public Map<String, String> uniqueErrorsForUpdate(Long id, RicettaDTO dto) {
        Map<String, String> errors = new HashMap<>();
        String nome = normNome(dto.nome());
        if (nome != null && getRepository().existsByNomeAndIdNot(nome, id)) {
            errors.put("nome", "Nome già presente in un'altra ricetta");
        }
        return errors;
    }

    public boolean createFromDto(RicettaDTO dto) {
        Ricetta r = mapper.toEntity(dto);
        
        saveFoto(dto.foto(), r);
        
        getRepository().save(r);
        return true;
    }

    public boolean updateFromDto(Long id, RicettaDTO dto) {
        Ricetta r = getByIdOrNull(id);
        if (r == null) {
            return false;
        }
        
        mapper.updateEntity(dto, r);
        
        if (dto.foto() != null && !dto.foto().isEmpty()) {
            saveFoto(dto.foto(), r);
        }
        
        getRepository().save(r);
        return true;
    }

    private void saveFoto(MultipartFile file, Ricetta r) {
        if (file != null && !file.isEmpty()) {
            try {
                Path uploadPath = Paths.get(UPLOAD_DIR);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
                Path filePath = uploadPath.resolve(fileName);

                Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                r.setFoto(fileName);
                
            } catch (IOException e) {
                throw new RuntimeException("Errore durante il salvataggio della foto: " + e.getMessage());
            }
        }
    }

    public boolean delete(Long id) {
        Ricetta r = getByIdOrNull(id);
        if (r == null) {
            return false;
        }
        deleteById(id);
        return true;
    }

    public RicettaDTO getDTOById(Long id) {
        Ricetta r = getByIdOrNull(id);
        return r == null ? null : mapper.toDTO(r);
    }

    public boolean belongsToUser(Long ricettaId, Long utenteId) {
        if (ricettaId == null || utenteId == null) return false;
        return getRepository().existsByIdAndUtenteId(ricettaId, utenteId);
    }

    public List<Ricetta> getAll() {
        return getRepository().findAll();
    }

    public List<Ricetta> getByUtente(Long utenteId) {
        return getRepository().findByUtenteId(utenteId);
    }

    public Ricetta getByIdWithIngredienti(Long id) {
        return getRepository().findWithIngredientiById(id).orElse(null);
    }
}