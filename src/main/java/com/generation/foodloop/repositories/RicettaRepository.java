package com.generation.foodloop.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.generation.foodloop.entities.Ricetta;
import com.generation.foodloop.entities.Utente;

public interface RicettaRepository extends JpaRepository<Ricetta, Long>{
    
    Optional<Ricetta> findById(Long id);
    Optional<Ricetta> findByNome(String nome);
    Optional<List<Ricetta>> findByNomeIngredienteAndUtente_id(String nome, Utente idUtente);
    Optional<List<Ricetta>> findByNomeIngrediente(String nome);

    boolean existsByNome(String nome);
    boolean existsByNomeAndId(String nome,Long id);

}
