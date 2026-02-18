package com.generation.foodloop.controllers;

import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.generation.foodloop.dto.IngredienteDTO;
import com.generation.foodloop.entities.Utente;
import com.generation.foodloop.services.IngredienteService;
import com.generation.foodloop.services.UtenteService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/ingredienti")
@RequiredArgsConstructor
public class IngredienteController {

    private final IngredienteService ingredienteService;
    private final UtenteService utenteService;

    // LISTA UTENTE LOGGATO
    @GetMapping
    public String listaIngredienti(Model model,
                                   Authentication authentication) {

        Utente user = (Utente) authentication.getPrincipal();
        Utente u = utenteService.getByIdWithIngredienti(user.getId());

        model.addAttribute("utente", u);
        model.addAttribute("ingredienti", u.getIngredienti());

        return "ingredienti/lista";
    }

    // FORM CREATE
    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("ingredienteDTO", IngredienteDTO.empty());
        model.addAttribute("mode", "create");
        return "ingredienti/form-dto";
    }

    // CREATE
    @PostMapping
    public String create(@Valid
                         @ModelAttribute("ingredienteDTO") IngredienteDTO dto,
                         BindingResult br,
                         Model model,
                         RedirectAttributes ra,
                         Authentication authentication) {

        if (br.hasErrors()) {
            model.addAttribute("mode", "create");
            return "ingredienti/form-dto";
        }

        Utente user = (Utente) authentication.getPrincipal();
        ingredienteService.createFromDto(dto, user.getId());

        ra.addFlashAttribute("success", "Ingrediente creato");
        return "redirect:/ingredienti";
    }

    // FORM EDIT
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id,
                           Model model,
                           RedirectAttributes ra) {

        IngredienteDTO dto = ingredienteService.getDTOById(id);

        if (dto == null) {
            ra.addFlashAttribute("error", "Ingrediente non trovato");
            return "redirect:/ingredienti";
        }

        model.addAttribute("ingredienteDTO", dto);
        model.addAttribute("mode", "edit");

        return "ingredienti/form-dto";
    }

    // UPDATE
    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @Valid
                         @ModelAttribute("ingredienteDTO") IngredienteDTO dto,
                         BindingResult br,
                         Model model,
                         RedirectAttributes ra) {

        dto.withId(id);

        Map<String, String> erroriUnicita =
                ingredienteService.uniqueErrorsForUpdate(id, dto);

        erroriUnicita.forEach((f, m) ->
                br.rejectValue(f, "duplicate", m));

        if (br.hasErrors()) {
            model.addAttribute("mode", "edit");
            return "ingredienti/form-dto";
        }

        boolean ok = ingredienteService.updateFromDto(id, dto);

        ra.addFlashAttribute(ok ? "success" : "error",
                ok ? "Ingrediente aggiornato"
                   : "Errore aggiornamento");

        return "redirect:/ingredienti";
    }

    // DELETE
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id,
                         RedirectAttributes ra) {

        boolean ok = ingredienteService.delete(id);

        ra.addFlashAttribute(ok ? "success" : "error",
                ok ? "Ingrediente eliminato"
                   : "Impossibile eliminare");

        return "redirect:/ingredienti";
    }
}
