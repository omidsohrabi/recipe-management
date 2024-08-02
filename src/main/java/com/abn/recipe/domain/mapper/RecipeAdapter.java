package com.abn.recipe.domain.mapper;

import com.abn.recipe.domain.model.Recipe;
import com.abn.recipe.repository.RecipeEntity;
import org.springframework.stereotype.Service;

@Service
public class RecipeAdapter {
    public RecipeEntity toEntity(Recipe recipe) {
        return RecipeEntity.builder()
                .id(recipe.getId())
                .name(recipe.getName())
                .vegetarian(recipe.isVegetarian())
                .servings(recipe.getServings())
                .ingredients(recipe.getIngredients())
                .instructions(recipe.getInstructions())
                .build();
    }

    public Recipe toDomain(RecipeEntity recipeEntity) {
        return Recipe.builder()
                .id(recipeEntity.getId())
                .name(recipeEntity.getName())
                .vegetarian(recipeEntity.isVegetarian())
                .servings(recipeEntity.getServings())
                .ingredients(recipeEntity.getIngredients())
                .instructions(recipeEntity.getInstructions())
                .build();
    }
}
