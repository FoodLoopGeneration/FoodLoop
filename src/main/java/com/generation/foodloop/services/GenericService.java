package com.generation.foodloop.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public abstract class GenericService<TipoID, E, R extends JpaRepository<E, TipoID>> {
    @Autowired
    private R repository;

    // getAll
    @Transactional(readOnly = true)
    public List<E> getAll() {
        return repository.findAll();
    }

    // findById
    @Transactional(readOnly = true)
    public Optional<E> findById(TipoID id) {
        return repository.findById(id);
    }

    @Transactional(readOnly = true)
    public E getByIdOrNull(TipoID id) {
        return repository.findById(id).orElse(null);
    }

    // save
    @Transactional
    public E save(E e){
        log.info("Salvataggio entità: "+e.getClass().getSimpleName());
        return repository.save(e);
    }

    // delete
    @Transactional
    public boolean deleteById(TipoID id){
        if(!repository.existsById(id)){
            return false;
        }
        log.info("Cancellazione dell'entità con id: "+id);
        repository.deleteById(id);
        return true;
    }
}
