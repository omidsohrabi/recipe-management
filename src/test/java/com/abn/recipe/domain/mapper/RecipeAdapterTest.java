package com.abn.recipe.domain.mapper;

import com.abn.recipe.domain.model.Recipe;
import com.abn.recipe.repository.RecipeEntity;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class RecipeAdapterTest {
    private final RecipeAdapter recipeAdapter = new RecipeAdapter();
    private final List<String> ingredients = List.of("Ingredient 1", "Ingredient 2");

    @Test
    void testToEntity() {
        Recipe recipe = Recipe.builder()
                .id(1L)
                .name("Test Recipe")
                .vegetarian(true)
                .servings(3)
                .ingredients(ingredients)
                .instructions("Instructions")
                .build();

        RecipeEntity recipeEntity = recipeAdapter.toEntity(recipe);

        assertThat(recipeEntity.getId()).isEqualTo(1L);
        assertThat(recipeEntity.getName()).isEqualTo("Test Recipe");
        assertThat(recipeEntity.isVegetarian()).isTrue();
        assertThat(recipeEntity.getServings()).isEqualTo(3);
        assertThat(recipeEntity.getIngredients()).isEqualTo(ingredients);
        assertThat(recipeEntity.getInstructions()).isEqualTo("Instructions");
    }

    @Test
    void testToDomain() {
        RecipeEntity recipeEntity = RecipeEntity.builder()
                .id(1L)
                .name("Test Recipe")
                .vegetarian(true)
                .servings(4)
                .ingredients(ingredients)
                .instructions("Instructions")
                .build();

        Recipe recipe = recipeAdapter.toDomain(recipeEntity);

        assertThat(recipe.getId()).isEqualTo(1L);
        assertThat(recipe.getName()).isEqualTo("Test Recipe");
        assertThat(recipe.isVegetarian()).isTrue();
        assertThat(recipe.getServings()).isEqualTo(4);
        assertThat(recipe.getIngredients()).isEqualTo(ingredients);
        assertThat(recipe.getInstructions()).isEqualTo("Instructions");
    }
}