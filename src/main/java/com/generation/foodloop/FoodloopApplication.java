package com.generation.foodloop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FoodloopApplication {

	public static void main(String[] args) {
		SpringApplication.run(FoodloopApplication.class, args);
	}

}
