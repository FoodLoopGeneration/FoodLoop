package com.generation.foodloop.controllers;

import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.generation.foodloop.dto.RicettaDTO;
import com.generation.foodloop.entities.Ricetta;
import com.generation.foodloop.entities.Utente;
import com.generation.foodloop.services.IngredienteService;
import com.generation.foodloop.services.RicettaService;
import com.generation.foodloop.services.UtenteService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/ricette")
@RequiredArgsConstructor
public class RicettaController {

    private final RicettaService ricettaService;
    private final IngredienteService ingredienteService;
    private final UtenteService utenteService;


    private void populateModel(Model model) {
        model.addAttribute("ingredienti", ingredienteService.getAll());
    }

    @GetMapping
    public String lista(Model model) {
        model.addAttribute("ricette", ricettaService.getAll());
        model.addAttribute("titolo", "Tutte le Ricette");
        model.addAttribute("isMieRicette", false);
        return "ricette/list";
    }

    @GetMapping("/mie")
    public String listaMie(Model model, Authentication authentication) {
        Utente user = (Utente) authentication.getPrincipal();
        model.addAttribute("ricette", ricettaService.getByUtente(user.getId()));
        model.addAttribute("titolo", "Le Mie Ricette");
        model.addAttribute("isMieRicette", true);
        return "ricette/list";
    }

    @GetMapping("/{id}")
    public String dettaglio(@PathVariable Long id, Model model, RedirectAttributes ra) {
        var ricetta = ricettaService.getByIdWithIngredienti(id);
        if (ricetta == null) {
            ra.addFlashAttribute("error", "Ricetta non trovata");
            return "redirect:/ricette";
        }
        model.addAttribute("ricetta", ricetta);
        return "ricette/details";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("ricettaDTO", RicettaDTO.empty()); 
        model.addAttribute("mode", "create");
        populateModel(model); 
        return "ricette/form-dto";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("ricettaDTO") RicettaDTO dto,
                         BindingResult br,
                         Model model,
                         Authentication auth,
                         RedirectAttributes ra) {
        
        if (br.hasErrors()) {
            log.warn("Errori di validazione: {}", br.getAllErrors());
            model.addAttribute("mode", "create");
            populateModel(model); 
            return "ricette/form-dto";
        }
        
        Utente user = (Utente) auth.getPrincipal();
        ricettaService.createFromDto(dto, user);
        ra.addFlashAttribute("success", "Ricetta creata con successo!");
        return "redirect:/ricette/mie";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model, RedirectAttributes ra, Authentication authentication) {
        Utente user = (Utente) authentication.getPrincipal();
        
        if (!ricettaService.belongsToUser(id, user.getId())) {
            ra.addFlashAttribute("error", "Non hai i permessi per questa ricetta");
            return "redirect:/ricette/mie";
        }

        RicettaDTO dto = ricettaService.getDTOById(id);
        if (dto == null) {
            ra.addFlashAttribute("error", "Ricetta non trovata");
            return "redirect:/ricette/mie";
        }

        model.addAttribute("ricettaDTO", dto);
        model.addAttribute("mode", "edit");
        populateModel(model); 
        return "ricette/form-dto";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("ricettaDTO") RicettaDTO dto,
                         BindingResult br,
                         Model model,
                         RedirectAttributes ra,
                         Authentication authentication) {

        Utente user = (Utente) authentication.getPrincipal();

        if (!ricettaService.belongsToUser(id, user.getId())) {
            ra.addFlashAttribute("error", "Operazione non consentita");
            return "redirect:/ricette/mie";
        }

        if (br.hasErrors()) {
            model.addAttribute("mode", "edit");
            populateModel(model); 
            return "ricette/form-dto";
        }

        boolean ok = ricettaService.updateFromDto(id, dto);
        ra.addFlashAttribute("success", ok ? "Ricetta aggiornata" : "Errore durante l'aggiornamento");
        return "redirect:/ricette/mie";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes ra, Authentication authentication) {
        Utente user = (Utente) authentication.getPrincipal();
        
        if (!ricettaService.belongsToUser(id, user.getId())) {
            ra.addFlashAttribute("error", "Operazione non consentita");
            return "redirect:/ricette/mie";
        }
        
        ricettaService.delete(id);
        ra.addFlashAttribute("success", "Ricetta eliminata");
        return "redirect:/ricette/mie";
    }

    @GetMapping("/suggerimenti")
    public String suggerimenti(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        Utente utente = utenteService.findByEmail(userDetails.getUsername()).orElse(null);
        if (utente == null) return "redirect:/login";

        List<Ricetta> suggerite = ricettaService.getSuggerimenti(utente.getId());
        model.addAttribute("ricette", suggerite);
        model.addAttribute("titolo", "Cosa puoi cucinare ora");
        model.addAttribute("isMieRicette", false);
        return "ricette/list";
    }
}