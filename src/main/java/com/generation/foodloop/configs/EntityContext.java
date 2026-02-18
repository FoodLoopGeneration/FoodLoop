package com.generation.foodloop.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import com.generation.foodloop.entities.Categoria;
import com.generation.foodloop.entities.Ingrediente;
import com.generation.foodloop.entities.Ricetta;
import com.generation.foodloop.entities.Utente;

@Configuration
public class EntityContext {

    @Bean
    @Scope("prototype")
    public Categoria categoria(){
        return new Categoria();
    }

    @Bean
    @Scope("prototype")
    public Ingrediente ingrediente(){
        return new Ingrediente();
    }

    @Bean
    @Scope("prototype")
    public Ricetta ricetta(){
        return new Ricetta();
    }

    @Bean
    @Scope("prototype")
    public Utente utente(){
        return new Utente();
    }
    
}
