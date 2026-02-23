package com.generation.foodloop.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.generation.foodloop.entities.Utente;
import com.generation.foodloop.services.UtenteService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/utenti")
@RequiredArgsConstructor
public class UtenteController {

private final UtenteService utenteService;

    private final static Long ID_UTENTE = 2l; 

    @GetMapping
    public String list(Model model){
        
        model.addAttribute("utenti", utenteService.getWithRuoliById(ID_UTENTE));
        log.debug("Lista utenti" + utenteService.getWithRuoliById(ID_UTENTE));
        return "utenti/list";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
       boolean ok = utenteService.deleteById(id);
       ra.addFlashAttribute(ok ? "success" : "error", ok ? "Utente eliminato con successo" : "Errore: Utente non trovato");
        return "redirect:/utenti";
    }
    
}
