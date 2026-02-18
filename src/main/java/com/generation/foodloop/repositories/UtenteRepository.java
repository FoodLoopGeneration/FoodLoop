package com.generation.foodloop.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import com.generation.foodloop.entities.Utente;

public interface UtenteRepository extends JpaRepository<Utente, Long> {
    @EntityGraph(attributePaths = "ruoli")
    Optional<Utente> findWithRuoloAndUsername(String username);

    Optional<Utente> findWithRuoloAndId(Long id);

    @EntityGraph(attributePaths = { "ruoli" })
    Optional<Utente> findByUsername(String username);

    @EntityGraph(attributePaths = { "ingredienti" })
    Optional<Utente> findWithIngredientiById(Long id);

    @EntityGraph(attributePaths = {"ruoli"})
    Optional<Utente> findByUsernameAndPassword(String username, String password);
}
