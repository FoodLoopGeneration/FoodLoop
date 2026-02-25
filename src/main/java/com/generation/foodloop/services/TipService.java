package com.generation.foodloop.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

@Service
public class TipService {

    public String getRandomTip() throws IOException {

        Path path = Paths.get(new ClassPathResource("tips.txt").getURI());
        
        List<String> lines = Files.readAllLines(path);

        if(lines.isEmpty()){

            return "Non sprecare il cibo!";

        }

        return lines.get(new Random().nextInt(lines.size()));

    } 
    
}
