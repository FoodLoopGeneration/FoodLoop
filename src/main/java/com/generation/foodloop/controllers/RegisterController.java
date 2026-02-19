package com.generation.foodloop.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
<<<<<<< HEAD
import org.springframework.validation.BindingResult;
=======
>>>>>>> origin/Alice
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.generation.foodloop.dto.UtenteDTO;
import com.generation.foodloop.services.UtenteService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import com.generation.foodloop.dto.UtenteDTO;

@Controller
@RequestMapping("/register")
@RequiredArgsConstructor
public class RegisterController {

    private final UtenteService utenteService;

    @GetMapping
<<<<<<< HEAD
    public String registerPage(Model model) {
        model.addAttribute("utenteDTO", UtenteDTO.empty());
=======
    public String registerPage(Model model){
        UtenteDTO user = UtenteDTO.empty(); 
        
        model.addAttribute("user", user);

>>>>>>> origin/Alice
        return "registration";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("utenteDTO") UtenteDTO dto,
            BindingResult br,
            RedirectAttributes ra) {

        if (br.hasErrors()) {
            return "registration";
        }

        boolean success = utenteService.createFromDto(dto);

        if (!success) {
            br.rejectValue("email", "error.utenteDTO", "Questa email è già registrata");
            return "registration";
        }

        ra.addFlashAttribute("success", "Registrazione avvenuta con successo! Accedi ora.");
        return "redirect:/login";
    }
}