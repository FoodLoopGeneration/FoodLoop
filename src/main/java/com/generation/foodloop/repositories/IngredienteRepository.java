package com.generation.foodloop.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.generation.foodloop.entities.Ingrediente;

public interface IngredienteRepository extends JpaRepository<Ingrediente, Long>{
    
    Optional<Ingrediente> findById(Long id);

    boolean existsById(Long id);

    boolean existsByNome(String nome);

    List<Ingrediente> findByUtenteId(Long utenteId);
    
    boolean existsByNomeAndIdNot(String nome, Long id);
    
}
