package com.abn.recipe.domain.exception;

import lombok.Getter;

@Getter
public class DuplicateRecipeException extends RuntimeException {
    public DuplicateRecipeException(String name) {
        super("Recipe with this name already exists: " + name);
    }
}
