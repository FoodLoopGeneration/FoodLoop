package com.generation.foodloop.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.generation.foodloop.dto.UtenteDTO;
import com.generation.foodloop.services.UtenteService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequestMapping("/register")
@RequiredArgsConstructor
public class RegisterController {

    private final UtenteService utenteService;
    
    @GetMapping
    public String registerPage(Model model){
        UtenteDTO user = UtenteDTO.empty(); 
        
        model.addAttribute("user", user);

        return "registration";
    }

    @PostMapping
    public String create(@Valid
                         @ModelAttribute("utenteDTO") UtenteDTO dto,
                         BindingResult br,
                         Model model,
                         RedirectAttributes ra,
                         Authentication authentication) {
        
         if (br.hasErrors()) {
            return "registration";
        }

        utenteService.createFromDto(dto);

        ra.addFlashAttribute("success", "Utente creato con successo!");
        return "redirect:/ingredienti";
    }
    
    

}