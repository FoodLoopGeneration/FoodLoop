package com.generation.foodloop.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.generation.foodloop.entities.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long>{
    
    Optional<Categoria> findById(Long id);
    Optional<Categoria> findByNome(String nome);

    boolean existsByNome(String nome);
    boolean existsByNomeAndId(String nome,Long id);

}
