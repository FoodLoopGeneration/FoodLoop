package com.generation.foodloop.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Controller
public class AppController {
    @GetMapping("/")
    public String home(Model model){
        model.addAttribute("title", "FoodLoop - Homepage");
        return "index";
    }

}
