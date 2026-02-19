package com.generation.foodloop.controllers;

import com.generation.foodloop.dto.CategoriaDTO;
import com.generation.foodloop.dto.IngredienteDTO;
import com.generation.foodloop.entities.Utente;
import com.generation.foodloop.entities.UnitaMisura;
import com.generation.foodloop.services.IngredienteService;
import com.generation.foodloop.services.UtenteService;
import com.generation.foodloop.services.CategoriaService;

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
@RequestMapping("/ingredienti")
@RequiredArgsConstructor
public class IngredienteController {

    private final IngredienteService ingredienteService;
    private final UtenteService utenteService;
    private final CategoriaService categoriaService;

    @ModelAttribute
    public void addFormAttributes(Model model, Authentication authentication) {
        if (authentication != null) {
            Utente user = (Utente) authentication.getPrincipal();
            model.addAttribute("unitaMisure", UnitaMisura.values());
            model.addAttribute("categorie", categoriaService.getByUtente(user.getId()));
        }
    }

    @GetMapping
    public String listaIngredienti(Model model, Authentication authentication) {
        Utente user = (Utente) authentication.getPrincipal();
        Utente u = utenteService.getByIdWithIngredienti(user.getId());
        model.addAttribute("utente", u);
        model.addAttribute("ingredienti", u.getIngredienti());
        return "ingredienti/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("ingredienteDTO", IngredienteDTO.empty());
        model.addAttribute("categoriaDTO", CategoriaDTO.empty());
        model.addAttribute("mode", "create");
        return "ingredienti/form-dto";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("ingredienteDTO") IngredienteDTO dto,
                         BindingResult br,
                         Model model,
                         Authentication auth,
                         RedirectAttributes ra) {

        if (br.hasErrors()) {
            model.addAttribute("mode", "create");
            return "ingredienti/form-dto";
        }

        Utente user = (Utente) auth.getPrincipal();
        ingredienteService.createFromDto(dto,user);
        ra.addFlashAttribute("success", "Ingrediente creato con successo");
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
            return "ingredienti/form-dto";
        }

        boolean ok = ingredienteService.updateFromDto(id, dto);
        ra.addFlashAttribute("success", ok ? "Ingrediente aggiornato" : "Errore aggiornamento");
        return "redirect:/ingredienti";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        boolean ok = ingredienteService.delete(id);
        ra.addFlashAttribute("success", ok ? "Ingrediente eliminato" : "Impossibile eliminare");
        return "redirect:/ingredienti";
    }
}