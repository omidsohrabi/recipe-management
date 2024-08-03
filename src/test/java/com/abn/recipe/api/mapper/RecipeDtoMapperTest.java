package com.abn.recipe.api.mapper;

import com.abn.recipe.api.dto.CreateRecipeDto;
import com.abn.recipe.api.dto.RecipeDto;
import com.abn.recipe.api.dto.RecipeSearchDto;
import com.abn.recipe.domain.model.Recipe;
import com.abn.recipe.domain.model.RecipeSearch;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class RecipeDtoMapperTest {

    private final RecipeDtoMapper recipeDtoMapper = new RecipeDtoMapper();
    private final List<String> ingredients = List.of("Ingredient 1", "Ingredient 2");

    @Test
    void testToDto() {
        Recipe recipe = Recipe.builder()
                .id(1L)
                .name("Test Recipe")
                .vegetarian(true)
                .servings(3)
                .ingredients(ingredients)
                .instructions("Instructions")
                .build();

        RecipeDto recipeDto = recipeDtoMapper.toDto(recipe);

        assertThat(recipeDto.getId()).isEqualTo(1L);
        assertThat(recipeDto.getName()).isEqualTo("Test Recipe");
        assertThat(recipeDto.isVegetarian()).isTrue();
        assertThat(recipeDto.getServings()).isEqualTo(3);
        assertThat(recipeDto.getIngredients()).isEqualTo(ingredients);
        assertThat(recipeDto.getInstructions()).isEqualTo("Instructions");
    }

    @Test
    void testToDomain() {
        CreateRecipeDto createRecipeDto = CreateRecipeDto.builder()
                .name("Test Recipe")
                .vegetarian(true)
                .servings(4)
                .ingredients(ingredients)
                .instructions("Instructions")
                .build();

        Recipe recipe = recipeDtoMapper.toDomain(createRecipeDto);

        assertThat(recipe.getName()).isEqualTo("Test Recipe");
        assertThat(recipe.isVegetarian()).isTrue();
        assertThat(recipe.getServings()).isEqualTo(4);
        assertThat(recipe.getIngredients()).isEqualTo(ingredients);
        assertThat(recipe.getInstructions()).isEqualTo("Instructions");
    }

    @Test
    void testToRecipeSearch() {
        List<String> includeIngredients = List.of("Include Ingredient 1", "Include Ingredient 2");
        List<String> excludeIngredients = List.of("Exclude Ingredient 1", "Exclude Ingredient 2");
        RecipeSearchDto searchDto = new RecipeSearchDto();
        searchDto.setName("Search Name");
        searchDto.setVegetarian(true);
        searchDto.setServings(2);
        searchDto.setIncludeIngredients(includeIngredients);
        searchDto.setExcludeIngredients(excludeIngredients);
        searchDto.setInstructionText("Instruction Text");

        RecipeSearch recipeSearch = recipeDtoMapper.toRecipeSearch(searchDto);

        assertThat(recipeSearch.getName()).isEqualTo("Search Name");
        assertThat(recipeSearch.getVegetarian()).isTrue();
        assertThat(recipeSearch.getServings()).isEqualTo(2);
        assertThat(recipeSearch.getIncludeIngredients()).isEqualTo(includeIngredients);
        assertThat(recipeSearch.getExcludeIngredients()).isEqualTo(excludeIngredients);
        assertThat(recipeSearch.getInstructionText()).isEqualTo("Instruction Text");
    }
}