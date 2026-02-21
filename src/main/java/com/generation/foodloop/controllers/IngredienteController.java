package com.generation.foodloop.controllers;

import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.generation.foodloop.dto.CategoriaDTO;
import com.generation.foodloop.dto.IngredienteDTO;
import com.generation.foodloop.entities.UnitaMisura;
import com.generation.foodloop.entities.Utente;
import com.generation.foodloop.services.CategoriaService;
import com.generation.foodloop.services.IngredienteService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/ingredienti")
@RequiredArgsConstructor
public class IngredienteController {

    private final IngredienteService ingredienteService;
    private final CategoriaService categoriaService;

    private void populateModel(Model model) {
        model.addAttribute("unita", UnitaMisura.values());
        model.addAttribute("categorie", categoriaService.getAll());

        if (!model.containsAttribute("categoriaDTO")) {
            model.addAttribute("categoriaDTO", CategoriaDTO.empty());
        }
    }

    @GetMapping
    public String lista(Model model, Authentication auth) {
        Utente user = (Utente) auth.getPrincipal();
        model.addAttribute("ingredienti", ingredienteService.getByUtente(user.getId()));
        return "ingredienti/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("ingredienteDTO", IngredienteDTO.empty());
        model.addAttribute("mode", "create");
        populateModel(model);
        return "ingredienti/form-dto";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("ingredienteDTO") IngredienteDTO dto,
                         BindingResult br,
                         Model model,
                         Authentication auth,
                         RedirectAttributes ra) {

        Map<String, String> erroriUnicita = ingredienteService.uniqueErrorsForCreate(dto);
        erroriUnicita.forEach((f, m) -> br.rejectValue(f, "duplicate", m));

        if (br.hasErrors()) {
            log.warn("Errori di validazione nella creazione ingrediente: {}", br.getAllErrors());
            model.addAttribute("mode", "create");
            populateModel(model); 
            return "ingredienti/form-dto";
        }

        Utente user = (Utente) auth.getPrincipal();
        ingredienteService.createFromDto(dto, user);
        ra.addFlashAttribute("success", "Ingrediente aggiunto alla dispensa!");
        return "redirect:/ingredienti";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model, RedirectAttributes ra) {
        IngredienteDTO dto = ingredienteService.getDTOById(id);
        if (dto == null) {
            ra.addFlashAttribute("error", "Ingrediente non trovato");
            return "redirect:/ingredienti";
        }
        model.addAttribute("ingredienteDTO", dto);
        model.addAttribute("mode", "edit");
        populateModel(model);
        return "ingredienti/form-dto";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("ingredienteDTO") IngredienteDTO dto,
                         BindingResult br,
                         Model model,
                         RedirectAttributes ra) {

        Map<String, String> erroriUnicita = ingredienteService.uniqueErrorsForUpdate(id, dto);
        erroriUnicita.forEach((f, m) -> br.rejectValue(f, "duplicate", m));

        if (br.hasErrors()) {
            model.addAttribute("mode", "edit");
            populateModel(model);
            return "ingredienti/form-dto";
        }

        boolean ok = ingredienteService.updateFromDto(id, dto);
        ra.addFlashAttribute("success", ok ? "Ingrediente aggiornato" : "Errore durante l'aggiornamento");
        return "redirect:/ingredienti";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        boolean ok = ingredienteService.delete(id);
        ra.addFlashAttribute("success", ok ? "Ingrediente rimosso" : "Impossibile eliminare l'ingrediente");
        return "redirect:/ingredienti";
    }
}