package com.abn.recipe.api.exception;

import lombok.Getter;

@Getter
public class RecipeNotFoundException extends RuntimeException {
    public RecipeNotFoundException(Long id) {
        super("Recipe not found with id: " + id);
    }
}
