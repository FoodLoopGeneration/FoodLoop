package com.generation.foodloop.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.generation.foodloop.entities.Ingrediente;

public interface IngredienteRepository extends JpaRepository<Ingrediente, Long>{
    
    Optional<Ingrediente> findById(Long id);

    boolean existsById(Long id);
    
}
