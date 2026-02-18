package com.generation.foodloop.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.generation.foodloop.dto.UtenteDTO;

@Controller
@RequestMapping("/register")
public class RegisterController {
    

    @GetMapping
    public String registerPage(){
        return "registration";
    }

}