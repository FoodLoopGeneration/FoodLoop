package com.generation.foodloop.controllers;

import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.generation.foodloop.dto.CategoriaDTO;
import com.generation.foodloop.dto.IngredienteDTO;
import com.generation.foodloop.services.CategoriaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/categoria")
@RequiredArgsConstructor
public class CategoriaController {

    private final CategoriaService categoriaService;

    @PostMapping
    public String create(@Valid @ModelAttribute("categoriaDTO") CategoriaDTO dto,
                         BindingResult br,
                         @ModelAttribute("ingredienteDTO") IngredienteDTO ingredienteCorrente,
                         Authentication auth,
                         RedirectAttributes ra) {


        Map<String, String> uniqueErrors = categoriaService.uniqueErrorsForCreate(dto);
        uniqueErrors.forEach((f, m) -> br.rejectValue(f, "duplicate", m));

        if (br.hasErrors()) {

            ra.addFlashAttribute("org.springframework.validation.BindingResult.categoriaDTO", br);
            ra.addFlashAttribute("categoriaDTO", dto);
            ra.addFlashAttribute("ingredienteDTO", ingredienteCorrente);
            ra.addFlashAttribute("error", "Errore nella creazione della categoria.");
            return "redirect:/ingredienti/new";
        }

        try {
            categoriaService.createFromDto(dto, null);
            ra.addFlashAttribute("success", "Categoria '" + dto.nome() + "' creata! Ora puoi selezionarla.");
        } catch (Exception e) {
            log.error("Errore salvataggio categoria", e);
            ra.addFlashAttribute("error", "Errore tecnico durante il salvataggio.");
        }

        ra.addFlashAttribute("ingredienteDTO", ingredienteCorrente);
        
        return "redirect:/ingredienti/new";
    }
}