package com.abn.recipe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.abn.recipe")
public class RecipeManager {
    public static void main(String[] args) {
        SpringApplication.run(RecipeManager.class, args);
    }
}