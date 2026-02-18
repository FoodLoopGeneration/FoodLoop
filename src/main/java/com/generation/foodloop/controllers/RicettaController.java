package com.generation.foodloop.controllers;

import com.generation.foodloop.dto.IngredienteDTO;
import com.generation.foodloop.dto.RicettaDTO;
import com.generation.foodloop.entities.Utente;
import com.generation.foodloop.services.IngredienteService;
import com.generation.foodloop.services.RicettaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/ricette")
@RequiredArgsConstructor
public class RicettaController {

    private final RicettaService ricettaService;
    private final IngredienteService ingredienteService;

    //tutte le ricette
    @GetMapping
    public String lista(Model model) {

        model.addAttribute("ricette",
                ricettaService.getAll());

        return "ricette/lista";
    }

    //ricette legate all'id utente
    @GetMapping("/mie")
    public String listaMie(Model model,
                           Authentication authentication) {

        Utente user = (Utente) authentication.getPrincipal();

        model.addAttribute("ricette",
                ricettaService.getByUtente(user.getId()));

        return "ricette/lista-mie";
    }

    //dettagli su id ricetta
    @GetMapping("/{id}")
    public String dettaglio(@PathVariable Long id,
                            Model model,
                            RedirectAttributes ra) {

        var ricetta = ricettaService.getByIdWithIngredienti(id);

        if (ricetta == null) {
            ra.addFlashAttribute("error",
                    "Ricetta non trovata");
            return "redirect:/ricette";
        }
        model.addAttribute("ricetta", ricetta);
        return "ricette/dettaglio";
    }

   //create
    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("ingredienteDTO", IngredienteDTO.empty());
        model.addAttribute("ingredienti",
                ingredienteService.getAll());
        model.addAttribute("mode", "create");
        return "ricette/form-dto";
    }

    //create
    @PostMapping
    public String create(@Valid
                         @ModelAttribute("ricettaDTO")
                         RicettaDTO dto,
                         BindingResult br,
                         Model model,
                         RedirectAttributes ra,
                         Authentication authentication) {

        if (br.hasErrors()) {

            model.addAttribute("ingredienti",
                    ingredienteService.getAll());
            model.addAttribute("mode", "create");
            return "ricette/form-dto";
        }
        Utente user = (Utente) authentication.getPrincipal();
        
        ricettaService.createFromDto(dto);
        ra.addFlashAttribute("success",
                "Ricetta creata");
        return "redirect:/ricette/mie";
    }

    //update ricetta
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id,
                           Model model,
                           RedirectAttributes ra,
                           Authentication authentication) {
        Utente user = (Utente) authentication.getPrincipal();
        if (!ricettaService.belongsToUser(id, user.getId())) {
            ra.addFlashAttribute("error",
                    "Operazione non consentita");
            return "redirect:/ricette/mie";
        }
        RicettaDTO dto = ricettaService.getDTOById(id);
        if (dto == null) {
            ra.addFlashAttribute("error",
                    "Ricetta non trovata");
            return "redirect:/ricette/mie";
        }
        model.addAttribute("ricettaDTO", dto);
        model.addAttribute("ingredienti",ingredienteService.getAll());
        model.addAttribute("mode", "edit");
        return "ricette/form-dto";
    }

    //update
    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @Valid
                         @ModelAttribute("ricettaDTO")
                         RicettaDTO dto,
                         BindingResult br,
                         Model model,
                         RedirectAttributes ra,
                         Authentication authentication) {

        Utente user = (Utente) authentication.getPrincipal();

        if (!ricettaService.belongsToUser(id, user.getId())) {
            ra.addFlashAttribute("error",
                    "Operazione non consentita");
            return "redirect:/ricette/mie";
        }
        dto.withId(id);
        if (br.hasErrors()) {
            model.addAttribute("ingredienti", ingredienteService.getAll());
            model.addAttribute("mode", "edit");
            return "ricette/form-dto";
        }
        boolean ok = ricettaService.updateFromDto(id, dto);

        ra.addFlashAttribute(ok ? "success" : "error",
                ok ? "Ricetta aggiornata"
                   : "Errore aggiornamento");

        return "redirect:/ricette/mie";
    }

    //delete
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id,
                         RedirectAttributes ra,
                         Authentication authentication) {

        Utente user = (Utente) authentication.getPrincipal();
        if (!ricettaService.belongsToUser(id, user.getId())) {
            ra.addFlashAttribute("error",
                    "Operazione non consentita");
            return "redirect:/ricette/mie";
        }
        boolean ok = ricettaService.delete(id);

        ra.addFlashAttribute(ok ? "success" : "error",
                ok ? "Ricetta eliminata"
                   : "Impossibile eliminare");
        return "redirect:/ricette/mie";
    }
}
