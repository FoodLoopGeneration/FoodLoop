package com.generation.foodloop.controllers;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.generation.foodloop.dto.CategoriaDTO;
import com.generation.foodloop.services.CategoriaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/categoria")
@RequiredArgsConstructor
public class CategoriaController {

    private final CategoriaService categoriaService;

    @GetMapping("/new")
    public String createForm(Model model) {

        model.addAttribute("categoriaDTO", CategoriaDTO.empty());
        model.addAttribute("mode", "create");

        // stessa pagina ingredienti/form-dto
        return "ingredienti/form-dto";
    }

    @PostMapping
    public String create(@Valid
                         @ModelAttribute("categoriaDTO") CategoriaDTO dto,
                         BindingResult br,
                         Model model,
                         RedirectAttributes ra) {

        // errori validazione standard
        if (br.hasErrors()) {
            model.addAttribute("mode", "create");
            return "ingredienti/form-dto";
        }

        // errori unicità
        Map<String,String> uniqueErrors =
                categoriaService.uniqueErrorsForCreate(dto);

        uniqueErrors.forEach((f,m) ->
                br.rejectValue(f, "duplicate", m));

        if (br.hasErrors()) {
            model.addAttribute("mode", "create");
            return "ingredienti/form-dto";
        }

        categoriaService.createFromDto(dto);

        ra.addFlashAttribute("success",
                "Categoria creata con successo");

        return "redirect:/ingredienti";
    }
}

